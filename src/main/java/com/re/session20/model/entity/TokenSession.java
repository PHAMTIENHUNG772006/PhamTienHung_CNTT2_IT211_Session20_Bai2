package com.re.session20.model.entity;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "token_sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String refreshTokenValue;

    private Boolean isRevoked;

    private Boolean isExpired;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}