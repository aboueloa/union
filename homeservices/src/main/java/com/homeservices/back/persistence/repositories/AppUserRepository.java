package com.homeservices.back.persistence.repositories;

import com.homeservices.back.persistence.models.AppUserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface AppUserRepository extends MongoRepository<AppUserEntity, String> {
    Optional<AppUserEntity> findByEmail(String email);

    @Override
    <S extends AppUserEntity> S insert(S entity);
}
