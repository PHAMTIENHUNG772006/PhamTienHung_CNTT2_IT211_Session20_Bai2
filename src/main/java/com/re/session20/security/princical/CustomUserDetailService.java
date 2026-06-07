package com.re.session20.security.princical;

import com.re.session20.model.entity.Account;
import com.re.session20.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Account account =
                accountRepository
                        .findAccountsByUsername(username)
                        .orElseThrow(() ->
                                new UsernameNotFoundException(
                                        "Không tồn tại tài khoản: " + username
                                ));

        List<SimpleGrantedAuthority> authorities =
                account.getRoles()
                        .stream()
                        .map(role ->
                                new SimpleGrantedAuthority(
                                        role.getRoleName()
                                ))
                        .toList();

        return CustomUserDetails.builder()
                .username(account.getUsername())
                .password(account.getPassword())
                .authorities(authorities)
                .build();
    }
}