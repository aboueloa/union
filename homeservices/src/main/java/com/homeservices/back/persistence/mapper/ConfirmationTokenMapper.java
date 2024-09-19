package com.homeservices.back.persistence.mapper;

import com.homeservices.back.domain.models.ConfirmationToken;
import com.homeservices.back.persistence.models.ConfirmationTokenEntity;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring"
)
public interface ConfirmationTokenMapper {
    ConfirmationToken toConfirmationToken(ConfirmationTokenEntity confirmationTokenEntity);
    ConfirmationTokenEntity toConfirmationTokenEntity(ConfirmationToken confirmationToken);
}
