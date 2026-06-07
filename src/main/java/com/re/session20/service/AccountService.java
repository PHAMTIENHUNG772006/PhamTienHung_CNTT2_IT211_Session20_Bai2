package com.re.session20.service;

import com.re.session20.model.dto.request.LoginRequest;
import com.re.session20.model.dto.request.RefreshTokenRequest;
import com.re.session20.model.dto.request.RegisterRequest;
import com.re.session20.model.dto.response.JwtResponse;
import com.re.session20.model.dto.response.RefreshTokenResponse;
import com.re.session20.model.entity.Account;

public interface AccountService {
    Account register(RegisterRequest registerRequest);
    JwtResponse login(LoginRequest request);
    RefreshTokenResponse refresh(
            RefreshTokenRequest request
    );

    void logout(String username);
}
