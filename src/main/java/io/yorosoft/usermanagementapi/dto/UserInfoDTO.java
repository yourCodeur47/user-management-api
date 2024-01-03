package io.yorosoft.usermanagementapi.dto;

import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.model.Token;
import io.yorosoft.usermanagementapi.model.User;

import java.util.List;

public record UserInfoDTO(
    Long id,
    String firstname,
    String lastname,
    String email,
    Role role,
    boolean enabled
) {

    public UserInfoDTO(User user) {
        this(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getRole(),
                user.isEnabled()
        );
    }

}
