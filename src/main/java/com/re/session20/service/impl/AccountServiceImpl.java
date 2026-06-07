package com.re.session20.service.impl;

import com.re.session20.model.custom_exception.UserNotFoundException;
import com.re.session20.model.dto.request.LoginRequest;
import com.re.session20.model.dto.request.RefreshTokenRequest;
import com.re.session20.model.dto.request.RegisterRequest;
import com.re.session20.model.dto.response.JwtResponse;
import com.re.session20.model.dto.response.RefreshTokenResponse;
import com.re.session20.model.entity.Account;
import com.re.session20.model.entity.Role;
import com.re.session20.model.entity.TokenSession;
import com.re.session20.repository.AccountRepository;
import com.re.session20.repository.RoleRepository;
import com.re.session20.repository.TokenRepository;
import com.re.session20.security.jwt.JwtProvider;
import com.re.session20.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final TokenRepository tokenRepository;
    private final RoleRepository roleRepository;

    @Override
    public Account register(RegisterRequest registerRequest) {

        List<Role> roles =
                registerRequest.getRoles()
                        .stream()
                        .map(roleName ->
                                roleRepository.findByRoleName(roleName)
                                        .orElseThrow(() ->
                                                new RuntimeException(
                                                        "Role không tồn tại: "
                                                                + roleName
                                                )
                                        )
                        )
                        .collect(Collectors.toList());

        Account account = Account.builder()
                .username(registerRequest.getUsername())
                .password(
                        passwordEncoder.encode(
                                registerRequest.getPassword()
                        )
                )
                .roles(roles)
                .isActive(true)
                .build();

        return accountRepository.save(account);
    }

    @Override
    public JwtResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Account account =
                accountRepository
                        .findAccountsByUsername(
                                request.getUsername()
                        )
                        .orElseThrow(
                                () -> new UserNotFoundException(
                                        "Account not found"
                                )
                        );

        String accessToken =
                jwtProvider.generateAccessToken(account);

        String refreshToken =
                jwtProvider.generateRefreshToken(account);

        saveToken(account, refreshToken);

        return JwtResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .username(account.getUsername())
                .authorities(
                        account.getRoles()
                                .stream()
                                .toList()
                )
                .build();
    }

    private void saveToken(
            Account account,
            String refreshToken
    ) {

        TokenSession tokenSession =
                TokenSession.builder()
                        .refreshTokenValue(refreshToken)
                        .isRevoked(false)
                        .isExpired(false)
                        .account(account)
                        .build();

        tokenRepository.save(tokenSession);
    }

    @Override
    public RefreshTokenResponse refresh(
            RefreshTokenRequest request
    ) {

        TokenSession tokenSession =
                tokenRepository
                        .findByRefreshTokenValue(
                                request.getRefreshToken()
                        )
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Refresh token không tồn tại"
                                )
                        );

        if (tokenSession.getIsRevoked()
                || tokenSession.getIsExpired()) {

            throw new RuntimeException(
                    "Refresh token đã bị thu hồi"
            );
        }

        String accessToken =
                jwtProvider.generateAccessToken(
                        tokenSession.getAccount()
                );

        return new RefreshTokenResponse(
                accessToken
        );
    }

    @Override
    public void logout(String username) {

        Account account =
                accountRepository
                        .findAccountsByUsername(username)
                        .orElseThrow(
                                () -> new UserNotFoundException(
                                        "Account not found"
                                )
                        );

        List<TokenSession> sessions =
                tokenRepository
                        .findByAccount_Id(
                                account.getId()
                        );

        sessions.stream()
                .forEach(session -> {
                    session.setIsRevoked(true);
                    session.setIsExpired(true);
                });

        tokenRepository.saveAll(sessions);

        SecurityContextHolder.clearContext();
    }
}