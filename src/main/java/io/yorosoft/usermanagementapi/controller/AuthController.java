package io.yorosoft.usermanagementapi.controller;

import io.yorosoft.usermanagementapi.dto.RegisterDTO;
import io.yorosoft.usermanagementapi.dto.UserInfoDTO;
import io.yorosoft.usermanagementapi.service.AuthService;
import io.yorosoft.usermanagementapi.utils.ResultDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping()
    public ResultDTO signup(@RequestBody @Valid RegisterDTO registerDTO) {
        var user =  authService.signup(registerDTO);
        return new ResultDTO(true, HttpStatus.OK.value(), "User created width success", new UserInfoDTO(user));
    }
}
