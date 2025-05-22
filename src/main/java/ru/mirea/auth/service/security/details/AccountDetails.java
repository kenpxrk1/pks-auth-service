package ru.mirea.auth.service.security.details;

import ru.mirea.auth.service.model.AccountEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountDetails implements UserDetails {
    private AccountEntity account;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(account.getRole()));
    }

    @Override
    public String getPassword() {
        return account.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return account.getPhoneNumber();
    }
}
