package ru.mirea.auth.service.service.account;

import ru.mirea.auth.service.model.AccountEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AccountService extends UserDetailsService {
    boolean existsByPhoneNumber(String phoneNumber);
    void save(AccountEntity account);
}
