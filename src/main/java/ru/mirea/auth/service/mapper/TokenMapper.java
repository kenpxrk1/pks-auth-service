package ru.mirea.auth.service.mapper;

import ru.mirea.auth.service.dto.response.LoginResponseDto;
import ru.mirea.auth.service.dto.response.RefreshTokenResponseDto;
import ru.mirea.auth.service.model.AccountEntity;
import ru.mirea.auth.service.model.TokenEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TokenMapper {
    @Mapping(target = "account", source = "accountEntity")
    @Mapping(target = "refreshToken", source = "refreshToken")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "expireTime", ignore = true)
    TokenEntity toTokenEntity(AccountEntity accountEntity, UUID refreshToken);

    @Mapping(target = "accessToken", source = "accessToken")
    @Mapping(target = "refreshToken", source = "refreshToken")
    LoginResponseDto toLoginResponse(String accessToken, UUID refreshToken);

    @Mapping(target = "accessToken", source = "accessToken")
    @Mapping(target = "refreshToken", source = "refreshToken")
    RefreshTokenResponseDto toRefreshReponse(String accessToken, UUID refreshToken);
}
