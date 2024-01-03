package io.yorosoft.usermanagementapi.dto;

import io.yorosoft.usermanagementapi.enums.Role;
import io.yorosoft.usermanagementapi.model.Token;
import io.yorosoft.usermanagementapi.model.User;

import java.util.List;

public record UserInfoDTO(
    Integer id,
    String firstname,
    String lastname,
    String email,
    Role role,
    List<TokenDTO> tokens
) {

    public UserInfoDTO(User user) {
        this(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getRole(),
                null
        );
    }

    public UserInfoDTO(User user, List<TokenDTO> tokens) {
        this(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getEmail(),
                user.getRole(),
                tokens
        );
    }

}
