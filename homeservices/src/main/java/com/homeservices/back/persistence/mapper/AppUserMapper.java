package com.homeservices.back.persistence.mapper;

import com.homeservices.back.persistence.models.AppUserEntity;
import com.homeservices.back.domain.models.AppUser;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface AppUserMapper {
    AppUserEntity toAppUserEntity(AppUser appUser);
    AppUser toAppUser(AppUserEntity appUserEntity);
}
