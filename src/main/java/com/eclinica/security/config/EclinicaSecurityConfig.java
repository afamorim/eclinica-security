package com.eclinica.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("eclinica")
@Data
public class EclinicaSecurityConfig {

    private Keycloak keycloak;

    @Data
    public static class Keycloak{
        private String clientId;
        private String secret;
        private String host;
    }
}
