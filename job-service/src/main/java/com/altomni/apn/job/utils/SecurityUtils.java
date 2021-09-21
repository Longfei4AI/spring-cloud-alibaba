package com.altomni.apn.job.utils;

import com.altomni.apn.common.config.AuthoritiesConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(extractPrincipal(securityContext.getAuthentication()));
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
            return springSecurityUser.getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            return (String) authentication.getPrincipal();
        }
        return null;
    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user.
     */
    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional
                .ofNullable(securityContext.getAuthentication())
                .filter(authentication -> authentication.getCredentials() instanceof String)
                .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
    }

    /**
     * Checks if the current user has a specific authority.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    public static boolean hasCurrentUserThisAuthority(String authority) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && getAuthorities(authentication).anyMatch(authority::equals);
    }

    private static Stream<String> getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority);
    }

    public static Long getTenantId() {
        try {
            return Long.valueOf(SecurityUtils.getCurrentUserLogin().get().split(",")[1]);
        } catch (Exception e) {
            //TODO: log exception
            return -1L;
        }
    }

    public static Long getUserId() {
        try {
            return Long.valueOf(SecurityUtils.getCurrentUserLogin().get().split(",")[0]);
        } catch (Exception e) {
            //TODO: log exception
            return -1L;
        }
    }

    public static boolean hasLogin(long userId){
        return userId != -1;
    }

    public static boolean hasPermission(Long tenantId) {
        return Objects.nonNull(tenantId) && tenantId.equals(SecurityUtils.getTenantId());
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the isUserInRole() method in the Servlet API
     *
     * @param authority the authority to check
     * @return true if the current user has the authority, false otherwise
     */
    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
        }
        return false;
    }

    public static boolean isCurrentTenant(Long tenantId){
        return getTenantId().equals(tenantId);
    }

    public static Long getTenantIdFromCreatedBy(String createdBy) {
        int index = createdBy.indexOf(",");
        return Long.parseLong(createdBy.substring(index+1, createdBy.length()));
    }

    public static boolean creatorBelongToCurrentTenant(String createdBy) {
        return SecurityUtils.getTenantIdFromCreatedBy(createdBy).equals(getTenantId());
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the isUserInRole() method in the Servlet API
     *
     * @return true if the current user is admin role
     */
    public static boolean isAdmin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority ->
                    (grantedAuthority.getAuthority().equals(AuthoritiesConstants.ADMIN))
                    || (grantedAuthority.getAuthority().equals(AuthoritiesConstants.TENANT_ADMIN))
                );
        }
        return false;
    }

    /**
     * get user id from create_by
     * @param createdBy
     * @return
     */
    public static Long getUserIdFromCreatedBy(String createdBy) {
        return Long.parseLong(createdBy.split(",")[0]);
    }

    public static boolean isCreatedByCurrentUser(String createdBy) {
        return SecurityUtils.getUserIdFromCreatedBy(createdBy).equals(getUserId());
    }

    /**
     * *******ONLY FOR REPORT!*******
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the isUserInRole() method in the Servlet API
     *
     * @return true if the current user is admin role
     */
    public static boolean isSuperUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority ->
                            (grantedAuthority.getAuthority().equals(AuthoritiesConstants.PRIVILEGE_REPORT))
                                    || (grantedAuthority.getAuthority().equals(AuthoritiesConstants.TENANT_ADMIN))
                    );
        }
        return false;
    }

    /**
     * *******ONLY FOR REPORT!*******
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the isUserInRole() method in the Servlet API
     *
     * @return true if the current user is admin role
     */
    public static boolean isTeamLeader() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                    .anyMatch(grantedAuthority ->
                            (grantedAuthority.getAuthority().equals(AuthoritiesConstants.PRIMARY_RECRUITER))
                    );
        }
        return false;
    }
}
