package ru.mirea.auth.service.service.account;

import ru.mirea.auth.service.model.AccountEntity;
import ru.mirea.auth.service.repository.AccountRepository;
import ru.mirea.auth.service.security.details.AccountDetails;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("getting account by phoneNumber={}", username);
        AccountEntity entity = findEntityByPhoneNumber(username);
        return new AccountDetails(entity);
    }

    @Override
    public void save(AccountEntity account) {
        accountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public boolean existsByPhoneNumber(String phoneNumber) {
        return accountRepository.existsAccountEntityByPhoneNumber(phoneNumber);
    }

    private AccountEntity findEntityByPhoneNumber(String phoneNumber) {
        return accountRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account with login '" + phoneNumber + "' not found"));
    }
}
