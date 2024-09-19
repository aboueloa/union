package com.homeservices.back.api.config;

import com.homeservices.back.domain.port.JwtPort;
import com.homeservices.back.domain.port.RegistrationPort;
import com.homeservices.back.domain.services.JwtService;
import com.homeservices.back.domain.services.RegistrationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@ConfigurationProperties
public class HomeServicesConfig {
    private static final int GMAIL_SMTP_PORT = 587;

    @Value("${SENDER_HOST}")
    private String host;

    @Value("${SENDER_USER}")
    private String user;

    @Value("${SENDER_PWD}")
    private String password;

    @Value("${homeservices.email.sender.debug}")
    private Boolean debug;
    @Bean
    RegistrationService registrationService(RegistrationPort registrationPort) {
        return new RegistrationService(registrationPort);
    }

    @Bean
    JavaMailSender javaMailSender() {
        System.out.print("user ================================== " + user);
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(GMAIL_SMTP_PORT);

        // Set up email config (using udeesa email)
        mailSender.setUsername(user);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", debug);
        return mailSender;
    }

    @Bean
    JwtService jwtService(JwtPort jwtPort) {
        return new JwtService(jwtPort);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
        configurer.setLocation(new FileSystemResource("/Users/aymanaboueloula/project/union/homeservices/src/main/resources/.env"));
        return configurer;
    }
}
