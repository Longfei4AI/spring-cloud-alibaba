package com.altomni.apn.authority.service;

import cn.hutool.core.util.RandomUtil;
import com.altomni.apn.authority.domain.Authority;
import com.altomni.apn.authority.domain.UserAdmin;
import com.altomni.apn.authority.exception.EmailAlreadyUsedException;
import com.altomni.apn.authority.exception.InvalidPasswordException;
import com.altomni.apn.authority.exception.UsernameAlreadyUsedException;
import com.altomni.apn.authority.repository.AuthorityRepository;
import com.altomni.apn.authority.repository.UserAdminRepository;
import com.altomni.apn.authority.service.dto.UserAdminDTO;
import com.altomni.apn.authority.utils.SecurityUtils;
import com.altomni.apn.common.config.AuthoritiesConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 * @author longfeiwang
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class UserAdminService extends UserSecurityService {

    @Resource
    private UserAdminRepository userAdminRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private AuthorityRepository authorityRepository;

    public Optional<UserAdmin> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userAdminRepository
            .findOneByActivationKey(key)
            .map(
                user -> {
                    // activate given user for the registration key.
                    user.setActivated(true);
                    user.setActivationKey(null);

                    log.debug("Activated user: {}", user);
                    return user;
                }
            );
    }

    public Optional<UserAdmin> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userAdminRepository
            .findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(
                user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setResetDate(null);

                    return user;
                }
            );
    }

    public Optional<UserAdmin> requestPasswordReset(String mail) {
        return userAdminRepository
            .findOneByEmailIgnoreCase(mail)
            .filter(UserAdmin::isActivated)
            .map(
                user -> {
                    user.setResetKey(RandomUtil.randomNumbers(20));
                    user.setResetDate(Instant.now());

                    return user;
                }
            );
    }

    public UserAdmin registerUser(UserAdminDTO userDTO, String password) {
        userAdminRepository
            .findOneByUsername(userDTO.getUsername().toLowerCase())
            .ifPresent(
                existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new UsernameAlreadyUsedException();
                    }
                }
            );
        userAdminRepository
            .findOneByEmailIgnoreCase(userDTO.getEmail())
            .ifPresent(
                existingUser -> {
                    boolean removed = removeNonActivatedUser(existingUser);
                    if (!removed) {
                        throw new EmailAlreadyUsedException();
                    }
                }
            );
        UserAdmin newUser = new UserAdmin();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setUsername(userDTO.getUsername().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            newUser.setEmail(userDTO.getEmail().toLowerCase());
        }
        newUser.setImageUrl(userDTO.getImageUrl());
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.randomNumbers(20));
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        userAdminRepository.save(newUser);
        log.debug("Created Information for UserAdmin: {}", newUser);
        return newUser;
    }

    private boolean removeNonActivatedUser(UserAdmin existingUser) {
        if (existingUser.isActivated()) {
            return false;
        }
        userAdminRepository.delete(existingUser);
        userAdminRepository.flush();
        return true;
    }

    public UserAdmin createUser(UserAdminDTO userDTO) {
        UserAdmin user = new UserAdmin();
        user.setUsername(userDTO.getUsername().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail().toLowerCase());
        }
        user.setImageUrl(userDTO.getImageUrl());

        String encryptedPassword = passwordEncoder.encode(RandomUtil.randomNumbers(20));
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.randomNumbers(20));
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO
                .getAuthorities()
                .stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userAdminRepository.save(user);

        log.debug("Created Information for UserAdmin: {}", user);
        return user;
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update.
     * @return updated user.
     */
    public Optional<UserAdminDTO> updateUser(UserAdminDTO userDTO) {
        return Optional
            .of(userAdminRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(
                user -> {

                    user.setUsername(userDTO.getUsername().toLowerCase());
                    user.setFirstName(userDTO.getFirstName());
                    user.setLastName(userDTO.getLastName());
                    if (userDTO.getEmail() != null) {
                        user.setEmail(userDTO.getEmail().toLowerCase());
                    }
                    user.setImageUrl(userDTO.getImageUrl());
                    user.setActivated(userDTO.isActivated());
                    Set<Authority> managedAuthorities = user.getAuthorities();
                    managedAuthorities.clear();
                    userDTO
                        .getAuthorities()
                        .stream()
                        .map(authorityRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .forEach(managedAuthorities::add);

                    log.debug("Changed Information for UserAdmin: {}", user);
                    return user;
                }
            )
            .map(UserAdminDTO::new);
    }

    public void deleteUser(String login) {
        userAdminRepository
            .findOneByUsername(login)
            .ifPresent(
                user -> {
                    userAdminRepository.delete(user);

                    log.debug("Deleted UserAdmin: {}", user);
                }
            );
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user.
     * @param lastName  last name of user.
     * @param email     email id of user.
     * @param imageUrl  image URL of user.
     */
    public void updateUser(String firstName, String lastName, String email, String imageUrl) {
        Optional.of(SecurityUtils.getCurrentUserLogin().get().getUsername())
            .flatMap(userAdminRepository::findOneByUsername)
            .ifPresent(
                user -> {
                    user.setFirstName(firstName);
                    user.setLastName(lastName);
                    if (email != null) {
                        user.setEmail(email.toLowerCase());
                    }
                    user.setImageUrl(imageUrl);

                    log.debug("Changed Information for UserAdmin: {}", user);
                }
            );
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        Optional.of(SecurityUtils.getCurrentUserLogin().get().getUsername())
            .flatMap(userAdminRepository::findOneByUsername)
            .ifPresent(
                user -> {
                    String currentEncryptedPassword = user.getPassword();
                    if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                        throw new InvalidPasswordException();
                    }
                    String encryptedPassword = passwordEncoder.encode(newPassword);
                    user.setPassword(encryptedPassword);

                    log.debug("Changed password for UserAdmin: {}", user);
                }
            );
    }

    @Transactional(readOnly = true)
    public Page<UserAdminDTO> getAllManagedUsers(Pageable pageable) {
        return userAdminRepository.findAll(pageable).map(UserAdminDTO::new);
    }

    @Transactional(readOnly = true)
    public Page<UserAdminDTO> getAllPublicUsers(Pageable pageable) {
        return userAdminRepository.findAllByIdNotNullAndActivatedIsTrue(pageable).map(UserAdminDTO::new);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional getUserWithAuthoritiesByLogin(String login) {
        return userAdminRepository.findOneWithAuthoritiesByUsername(login);
    }

    @Transactional(readOnly = true)
    public Optional<UserAdmin> getUserWithAuthorities() {
        return Optional.of(SecurityUtils.getCurrentUserLogin().get().getUsername()).flatMap(userAdminRepository::findOneWithAuthoritiesByUsername);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    /*@Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(
                user -> {
                    log.debug("Deleting not activated user {}", user.getUsername());
                    userRepository.delete(user);
                }
            );
    }*/


    @Transactional(readOnly = true)
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }

}
