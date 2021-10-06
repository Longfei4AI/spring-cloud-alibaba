package com.altomni.apn.authority.web.rest;

import com.altomni.apn.authority.domain.UserAdmin;
import com.altomni.apn.authority.exception.EmailAlreadyUsedException;
import com.altomni.apn.authority.exception.InvalidPasswordException;
import com.altomni.apn.authority.exception.LoginAlreadyUsedException;
import com.altomni.apn.authority.repository.UserAdminRepository;
import com.altomni.apn.authority.service.UserAdminService;
import com.altomni.apn.authority.service.dto.PasswordChangeDTO;
import com.altomni.apn.authority.service.dto.UserAdminDTO;
import com.altomni.apn.authority.utils.SecurityUtils;
import com.altomni.apn.authority.web.rest.vm.KeyAndPasswordVM;
import com.altomni.apn.authority.web.rest.vm.LoginVM;
import com.altomni.apn.authority.web.rest.vm.ManagedUserAdminVM;
import com.altomni.apn.common.dto.LoginUserDTO;
import io.micrometer.core.annotation.Timed;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

/**
 * REST controller for managing the current user's account.
 */
@Slf4j
@RestController
@RequestMapping("/api/v3/account/admin")
public class AccountAdminResource {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    @Resource
    private UserAdminRepository userAdminRepository;

    @Resource
    private UserAdminService userAdminService;

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserAdminVM managedUserVM) {
        if (isPasswordLengthInvalid(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        UserAdmin user = userAdminService.registerUser(managedUserVM, managedUserVM.getPassword());
        //mailService.sendActivationEmail(user); TODO
    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<UserAdmin> user = userAdminService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    @ApiOperation(value = "UserAdmin Login", response = LoginUserDTO.class)
    @PostMapping(path = "/login", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Timed
    public ResponseEntity<LoginUserDTO> login(@Valid @RequestBody LoginVM loginVM) {
        LoginUserDTO loginUser = userAdminService.login(loginVM);
        return ResponseEntity.ok(loginUser);
    }


    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/")
    public UserAdminDTO getAccount() {
        return userAdminService
            .getUserWithAuthorities()
            .map(UserAdminDTO::new)
            .orElseThrow(() -> new AccountResourceException("UserAdmin could not be found"));
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/")
    public void saveAccount(@Valid @RequestBody UserAdminDTO userDTO) {
        LoginUserDTO userLogin = SecurityUtils
            .getCurrentUserLogin()
            .orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<UserAdmin> existingUser = userAdminRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getUsername().equalsIgnoreCase(userLogin.getUsername()))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<UserAdmin> user = userAdminRepository.findOneByUsername(userLogin.getUsername());
        if (!user.isPresent()) {
            throw new AccountResourceException("UserAdmin could not be found");
        }
        userAdminService.updateUser(
            userDTO.getFirstName(),
            userDTO.getLastName(),
            userDTO.getEmail(),
            userDTO.getImageUrl()
        );
    }

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (isPasswordLengthInvalid(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userAdminService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     */
    @PostMapping(path = "/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
        Optional<UserAdmin> user = userAdminService.requestPasswordReset(mail);
        if (user.isPresent()) {
            //mailService.sendPasswordResetMail(user.get()); TODO
        } else {
            // Pretend the request has been successful to prevent checking which emails really exist
            // but log that an invalid attempt has been made
            log.warn("Password reset requested for non existing mail");
        }
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (isPasswordLengthInvalid(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<UserAdmin> user = userAdminService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean isPasswordLengthInvalid(String password) {
        return (
            StringUtils.isEmpty(password) ||
            password.length() < ManagedUserAdminVM.PASSWORD_MIN_LENGTH ||
            password.length() > ManagedUserAdminVM.PASSWORD_MAX_LENGTH
        );
    }
}
