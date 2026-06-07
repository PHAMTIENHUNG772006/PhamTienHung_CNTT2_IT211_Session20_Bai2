package com.re.session20.controller;

import com.re.session20.model.dto.request.LoginRequest;
import com.re.session20.model.dto.request.RefreshTokenRequest;
import com.re.session20.model.dto.request.RegisterRequest;
import com.re.session20.model.dto.response.ApiDataResponse;
import com.re.session20.model.dto.response.JwtResponse;
import com.re.session20.model.dto.response.RefreshTokenResponse;
import com.re.session20.model.entity.Account;
import com.re.session20.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gallery/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AccountService accountService;

    @PostMapping("/register")
    public ResponseEntity<ApiDataResponse<Account>> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        return new ResponseEntity<>(
                new ApiDataResponse<>(
                        true,
                        "Đăng ký thành công",
                        accountService.register(request),
                        null,
                        HttpStatus.CREATED
                ),
                HttpStatus.CREATED
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiDataResponse<JwtResponse>> login(
            @RequestBody LoginRequest request
    ) {

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Đăng nhập thành công",
                        accountService.login(request),
                        null,
                        HttpStatus.OK
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiDataResponse<RefreshTokenResponse>> refresh(
            @RequestBody RefreshTokenRequest request
    ) {

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Làm mới Access Token thành công",
                        accountService.refresh(request),
                        null,
                        HttpStatus.OK
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiDataResponse<String>> logout(
            Authentication authentication
    ) {

        accountService.logout(
                authentication.getName()
        );

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Đăng xuất thành công",
                        "SUCCESS",
                        null,
                        HttpStatus.OK
                )
        );
    }
}