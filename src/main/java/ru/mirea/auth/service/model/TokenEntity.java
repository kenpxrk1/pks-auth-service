package ru.mirea.auth.service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "token")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "refresh_token", unique = true)
    private UUID refreshToken;

    @Column(name = "expire_time")
    private OffsetDateTime expireTime;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    private AccountEntity account;
}
