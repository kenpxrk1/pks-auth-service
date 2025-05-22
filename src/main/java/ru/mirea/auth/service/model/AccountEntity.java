package ru.mirea.auth.service.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "account")
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", unique = true)
    private UUID externalId;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "role")
    private String role;

    @Column(name = "is_2fa_enabled")
    private boolean is_2fa_enabled;

    @Column(name = "is_blocked")
    private boolean isBlocked;

    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "account", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private List<TokenEntity> token;
}
