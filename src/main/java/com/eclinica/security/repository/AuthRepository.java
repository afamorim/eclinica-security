package com.eclinica.security.repository;

import com.eclinica.security.config.EclinicaSecurityConfig;
import com.eclinica.security.model.KeycloakAPIs;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Repository
@Slf4j
public class AuthRepository {

    @Autowired
    private EclinicaSecurityConfig eclinicaSecurityConfig;

    private final String ACCESS_TOKEN = "access_token";

    public String getToken()  {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String auth = eclinicaSecurityConfig.getKeycloak().getClientId() + ":" + eclinicaSecurityConfig.getKeycloak().getSecret();
        byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );
        httpHeaders.set( HttpHeaders.AUTHORIZATION, authHeader );


        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("grant_type", AuthorizationGrantType.CLIENT_CREDENTIALS.getValue());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, httpHeaders);

        log.info("chamada para retorno do token " + eclinicaSecurityConfig.getKeycloak().getHost());

        ResponseEntity<String> response = restTemplate.postForEntity(eclinicaSecurityConfig.getKeycloak().getHost() + KeycloakAPIs.TOKEN_API.getValue(), request, String.class);

        if (response.getStatusCodeValue() == 200){
            try {
                JsonFactory factory = new JsonFactory();

                ObjectMapper mapper = new ObjectMapper(factory);

                JsonNode rootNode = mapper.readTree(response.getBody());
                Iterator<Map.Entry<String,JsonNode>> fieldsIterator = rootNode.fields();
                AtomicReference<String> token_return = new AtomicReference<String>();
                fieldsIterator.forEachRemaining(entry -> {
                    if (entry.getValue().equals(ACCESS_TOKEN)){
                        token_return.set(entry.getValue().asText());
                    }
                });
                if (Objects.nonNull(token_return.get())){
                    return token_return.get();
                }
            } catch (JsonProcessingException e) {
                log.error("Error json deserialization", e);
            }
        }

        return null;
    }
}
