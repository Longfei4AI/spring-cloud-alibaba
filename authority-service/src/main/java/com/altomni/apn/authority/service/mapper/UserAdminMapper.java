package com.altomni.apn.authority.service.mapper;

import com.altomni.apn.authority.domain.Authority;
import com.altomni.apn.authority.domain.UserAdmin;
import com.altomni.apn.authority.service.dto.UserAdminDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link UserAdmin} and its DTO called {@link UserAdminDTO}.
 *
 * Normal mappers are generated using MapStruct, this one is hand-coded as MapStruct
 * support is still in beta, and requires a manual step with an IDE.
 */
@Service
public class UserAdminMapper {

    public List<UserAdminDTO> usersToUserDTOs(List<UserAdmin> users) {
        return users.stream().filter(Objects::nonNull).map(this::userToUserDTO).collect(Collectors.toList());
    }

    public UserAdminDTO userToUserDTO(UserAdmin user) {
        return new UserAdminDTO(user);
    }

    public List<UserAdminDTO> usersToAdminUserDTOs(List<UserAdmin> users) {
        return users.stream().filter(Objects::nonNull).map(this::userToAdminUserDTO).collect(Collectors.toList());
    }

    public UserAdminDTO userToAdminUserDTO(UserAdmin user) {
        return new UserAdminDTO(user);
    }

    public List<UserAdmin> userDTOsToUsers(List<UserAdminDTO> userDTOs) {
        return userDTOs.stream().filter(Objects::nonNull).map(this::userDTOToUser).collect(Collectors.toList());
    }

    public UserAdmin userDTOToUser(UserAdminDTO userDTO) {
        if (userDTO == null) {
            return null;
        } else {
            UserAdmin user = new UserAdmin();
            user.setId(userDTO.getId());
            user.setUsername(userDTO.getUsername());
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setEmail(userDTO.getEmail());
            user.setImageUrl(userDTO.getImageUrl());
            user.setActivated(userDTO.isActivated());
            Set<Authority> authorities = this.authoritiesFromStrings(userDTO.getAuthorities());
            user.setAuthorities(authorities);
            return user;
        }
    }

    private Set<Authority> authoritiesFromStrings(Set<String> authoritiesAsString) {
        Set<Authority> authorities = new HashSet<>();

        if (authoritiesAsString != null) {
            authorities =
                authoritiesAsString
                    .stream()
                    .map(
                        string -> {
                            Authority auth = new Authority();
                            auth.setName(string);
                            return auth;
                        }
                    )
                    .collect(Collectors.toSet());
        }

        return authorities;
    }

    public UserAdmin userFromId(Long id) {
        if (id == null) {
            return null;
        }
        UserAdmin user = new UserAdmin();
        user.setId(id);
        return user;
    }

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public UserAdminDTO toDtoId(UserAdmin user) {
        if (user == null) {
            return null;
        }
        UserAdminDTO userDto = new UserAdminDTO();
        userDto.setId(user.getId());
        return userDto;
    }

    @Named("idSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    public Set<UserAdminDTO> toDtoIdSet(Set<UserAdmin> users) {
        if (users == null) {
            return Collections.emptySet();
        }

        Set<UserAdminDTO> userSet = new HashSet<>();
        for (UserAdmin userEntity : users) {
            userSet.add(this.toDtoId(userEntity));
        }

        return userSet;
    }

    @Named("login")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    public UserAdminDTO toDtoLogin(UserAdmin user) {
        if (user == null) {
            return null;
        }
        UserAdminDTO userDto = new UserAdminDTO();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        return userDto;
    }

    @Named("loginSet")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    public Set<UserAdminDTO> toDtoLoginSet(Set<UserAdmin> users) {
        if (users == null) {
            return Collections.emptySet();
        }

        Set<UserAdminDTO> userSet = new HashSet<>();
        for (UserAdmin userEntity : users) {
            userSet.add(this.toDtoLogin(userEntity));
        }

        return userSet;
    }
}
