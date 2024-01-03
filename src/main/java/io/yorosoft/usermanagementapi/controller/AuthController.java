package io.yorosoft.usermanagementapi.controller;

import io.yorosoft.usermanagementapi.dto.RegisterDTO;
import io.yorosoft.usermanagementapi.dto.UserInfoDTO;
import io.yorosoft.usermanagementapi.service.AuthService;
import io.yorosoft.usermanagementapi.service.RefreshTokenService;
import io.yorosoft.usermanagementapi.utils.Result;
import io.yorosoft.usermanagementapi.utils.StatusCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public Result signup(@RequestBody RegisterDTO registerDTO) {
        var user =  authService.signup(registerDTO);
        return new Result(true, StatusCode.SUCCESS, "User created width succes", new UserInfoDTO(user));
    }
}
