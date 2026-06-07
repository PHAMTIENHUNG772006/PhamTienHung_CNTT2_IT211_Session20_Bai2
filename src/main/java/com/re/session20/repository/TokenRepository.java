package com.re.session20.repository;

import com.re.session20.model.entity.TokenSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository
        extends JpaRepository<TokenSession, Long> {

    Optional<TokenSession> findByRefreshTokenValue(
            String refreshTokenValue
    );


    List<TokenSession> findByAccount_Id(
            Long accountId
    );
}