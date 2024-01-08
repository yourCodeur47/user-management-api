package io.yorosoft.usermanagementapi.config;

import io.yorosoft.usermanagementapi.dto.RegisterDTO;
import io.yorosoft.usermanagementapi.enums.Role;

public class TestConfigurer {

    protected RegisterDTO getRegisterDTO(String firstname, String lastname, String email, String password, Role role) {
        return new RegisterDTO(firstname, lastname, email, password, role);
    }


}
