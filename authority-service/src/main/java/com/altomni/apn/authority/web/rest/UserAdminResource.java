package com.altomni.apn.authority.web.rest;

import com.altomni.apn.authority.config.Constants;
import com.altomni.apn.authority.domain.UserAdmin;
import com.altomni.apn.authority.exception.EmailAlreadyUsedException;
import com.altomni.apn.authority.exception.LoginAlreadyUsedException;
import com.altomni.apn.authority.repository.UserAdminRepository;
import com.altomni.apn.authority.service.UserAdminService;
import com.altomni.apn.authority.service.dto.UserAdminDTO;
import com.altomni.apn.common.config.AuthoritiesConstants;
import com.altomni.apn.common.errors.BadRequestAlertException;
import com.altomni.apn.common.utils.HeaderUtil;
import com.altomni.apn.common.utils.PaginationUtil;
import com.altomni.apn.common.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link UserAdmin} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between UserAdmin and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 * @author longfeiwang
 */
@Slf4j
@RestController
@RequestMapping("/api/v3/admin")
public class UserAdminResource {

    private static final List<String> ALLOWED_ORDERED_PROPERTIES = Collections.unmodifiableList(
        Arrays.asList(
            "id",
            "username",
            "firstName",
            "lastName",
            "email",
            "activated",
            "createdBy",
            "createdDate",
            "lastModifiedBy",
            "lastModifiedDate"
        )
    );

    @Value("${spring.application.name}")
    private String applicationName;

    @Resource
    private UserAdminService userAdminService;

    @Resource
    private UserAdminRepository userAdminRepository;

    //private final MailService mailService;


    /**
     * {@code POST  /admin/users}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserAdmin> createUser(@Valid @RequestBody UserAdminDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save UserAdmin : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userAdminRepository.findOneByUsername(userDTO.getUsername().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userAdminRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            UserAdmin newUser = userAdminService.createUser(userDTO);
            //mailService.sendCreationEmail(newUser); TODO
            return ResponseEntity
                .created(new URI("/api/admin/users/" + newUser.getUsername()))
                .headers(HeaderUtil.createAlert(applicationName, "userManagement.created", newUser.getUsername()))
                .body(newUser);
        }
    }

    /**
     * {@code PUT /admin/users} : Updates an existing UserAdmin.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserAdminDTO> updateUser(@Valid @RequestBody UserAdminDTO userDTO) {
        log.debug("REST request to update UserAdmin : {}", userDTO);
        Optional<UserAdmin> existingUser = userAdminRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userAdminRepository.findOneByUsername(userDTO.getUsername().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<UserAdminDTO> updatedUser = userAdminService.updateUser(userDTO);

        return ResponseUtil.wrapOrNotFound(
            updatedUser,
            HeaderUtil.createAlert(applicationName, "userManagement.updated", userDTO.getUsername())
        );
    }

    /**
     * {@code GET /admin/users} : get all users with all the details - calling this are only allowed for the administrators.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<UserAdminDTO>> getAllUsers(Pageable pageable) {
        log.debug("REST request to get all Users");
        if (!onlyContainsAllowedProperties(pageable)) {
            return ResponseEntity.badRequest().build();
        }

        final Page<UserAdminDTO> page = userAdminService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    private boolean onlyContainsAllowedProperties(Pageable pageable) {
        return pageable.getSort().stream().map(Sort.Order::getProperty).allMatch(ALLOWED_ORDERED_PROPERTIES::contains);
    }

    /**
     * {@code GET /admin/users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{login}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserAdminDTO> getUser(@PathVariable @Pattern(regexp = Constants.LOGIN_REGEX) String login) {
        log.debug("REST request to get UserAdmin : {}", login);
        return ResponseUtil.wrapOrNotFound(userAdminService.getUserWithAuthoritiesByLogin(login).map(user -> new UserAdminDTO((UserAdmin) user)));
    }

    /**
     * {@code DELETE /admin/users/:login} : delete the "login" UserAdmin.
     *
     * @param login the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{login}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable @Pattern(regexp = Constants.LOGIN_REGEX) String login) {
        log.debug("REST request to delete UserAdmin: {}", login);
        userAdminService.deleteUser(login);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "userManagement.deleted", login)).build();
    }
}
