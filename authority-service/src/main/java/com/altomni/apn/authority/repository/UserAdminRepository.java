package com.altomni.apn.authority.repository;

import com.altomni.apn.authority.domain.UserAdmin;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link UserAdmin} entity.
 */
@Repository
public interface UserAdminRepository extends JpaRepository<UserAdmin, Long> {
    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";

    Optional<UserAdmin> findOneByActivationKey(String activationKey);

    List<UserAdmin> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);

    Optional<UserAdmin> findOneByResetKey(String resetKey);

    Optional<UserAdmin> findOneByEmailIgnoreCase(String email);

    Optional<UserAdmin> findOneByUsername(String username);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<UserAdmin> findOneWithAuthoritiesByUsername(String username);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<UserAdmin> findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    Optional<UserAdmin> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Page<UserAdmin> findAllByIdNotNullAndActivatedIsTrue(Pageable pageable);
}
