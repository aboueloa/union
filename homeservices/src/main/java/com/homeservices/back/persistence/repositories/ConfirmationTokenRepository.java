package com.homeservices.back.persistence.repositories;

import com.homeservices.back.persistence.models.ConfirmationTokenEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ConfirmationTokenRepository extends MongoRepository<ConfirmationTokenEntity, String> {
    Optional<ConfirmationTokenEntity> findById(String token);
}
