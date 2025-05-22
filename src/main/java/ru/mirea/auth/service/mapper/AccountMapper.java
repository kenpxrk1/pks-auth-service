package ru.mirea.auth.service.mapper;

import ru.mirea.auth.service.dto.request.RegisterRequestDto;
import ru.mirea.auth.service.model.AccountEntity;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.crypto.password.PasswordEncoder;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "externalId", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "passwordHash", expression = "java(passwordEncoder.encode(dto.getPassword()))")
    @Mapping(target = "role", constant = "USER")
    @Mapping(target = "_2fa_enabled", constant = "false")
    @Mapping(target = "blocked", constant = "false")
    @Mapping(target = "token", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AccountEntity toEntity(RegisterRequestDto dto, @Context PasswordEncoder passwordEncoder);
}
