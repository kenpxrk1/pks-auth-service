package ru.mirea.auth.service.repository;

import ru.mirea.auth.service.model.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByPhoneNumber(String phoneNumber);
    boolean existsAccountEntityByPhoneNumber(String phoneNumber);
}
