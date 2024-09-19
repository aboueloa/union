package com.homeservices.back.persistence.adapter;

import com.homeservices.back.api.dto.EmailValidator;
import com.homeservices.back.domain.exception.UserConfirmationException;
import com.homeservices.back.domain.helper.Utils;
import com.homeservices.back.domain.models.AppUser;
import com.homeservices.back.domain.port.EmailPort;
import com.homeservices.back.domain.port.RegistrationPort;
import com.homeservices.back.persistence.mapper.AppUserMapper;
import com.homeservices.back.persistence.models.AppUserEntity;
import com.homeservices.back.persistence.models.ConfirmationTokenEntity;
import com.homeservices.back.persistence.repositories.AppUserRepository;
import com.homeservices.back.persistence.repositories.ConfirmationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService, RegistrationPort {
    private final AppUserRepository appUserRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailValidator emailValidator;
    private final AppUserMapper appUserMapper;
    private final EmailPort emailPort;
    private Environment environment;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
    @Transactional
    public void singUpUser(AppUserEntity toAppUserEntity) {
        Optional<AppUserEntity> user = appUserRepository.findByEmail(toAppUserEntity.getEmail());
        boolean userExists = user.isPresent();
        boolean userEnabled = userExists && user.get().isEnabled();
        if (userEnabled) {
            throw new IllegalStateException("email already taken");
        }

        AppUserEntity appUserEntity;
        if (!userExists) {
            String encodedPwd = bCryptPasswordEncoder.encode(toAppUserEntity.getPwd());
            toAppUserEntity.setPwd(encodedPwd);
            appUserEntity = appUserRepository.insert(toAppUserEntity);
        } else {
            appUserEntity = user.get();
        }

        if (userExists) {
            ConfirmationTokenEntity confirmationTokenEntity = confirmationTokenRepository.findAll().stream().filter(e -> e.getAppUserId().equals(appUserEntity.getId()))
                    .findFirst().orElseThrow(() -> new UsernameNotFoundException("A user exist but no confirmation token is assigned"));
            confirmationTokenRepository.delete(confirmationTokenEntity);
        }

        String token = UUID.randomUUID().toString();
        var confirmationToken = ConfirmationTokenEntity.builder()
                        .token(token)
                        .createdAt(LocalDateTime.now())
                        .expiredAt(LocalDateTime.now().plusMinutes(1))
                        .appUserId(appUserEntity.getId())
                .build();

        ConfirmationTokenEntity tokenSaved = confirmationTokenRepository.insert(confirmationToken);

        String urlPrefix = environment.getProperty("env.url");
        String url = urlPrefix +
                "/api/v1/registration/confirm?token=" +
                tokenSaved.getId();

        emailPort.send(appUserEntity.getEmail(), Utils.buildEmail(appUserEntity.getFirstName(), url));
    }

    @Override
    public void register(AppUser appUser) {
        boolean isValidEmail = emailValidator.test(appUser.getEmail());
        if (!isValidEmail) {
            throw new IllegalStateException("email not valid");
        }
        singUpUser(appUserMapper.toAppUserEntity(appUser));
    }

    @Override
    @Transactional
    public void confirm(String token) throws UserConfirmationException {
        ConfirmationTokenEntity confirmationToken = confirmationTokenRepository.findById(token).orElseThrow(() -> new UserConfirmationException("Token not found"));
        if (confirmationToken.getConfirmedAt() != null) {
            throw new UserConfirmationException("Email already confirmed");
        }
        LocalDateTime expiredAt = confirmationToken.getExpiredAt();
        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new UserConfirmationException("Token expired");
        }
        confirmationToken.setConfirmedAt(LocalDateTime.now());
        confirmationTokenRepository.save(confirmationToken);
        AppUserEntity user = appUserRepository.findById(confirmationToken.getAppUserId()).get();
        user.setEnabled(true);
        appUserRepository.save(user);
    }
}
