package com.eclinica.security.repository;

import com.eclinica.security.config.EclinicaSecurityConfig;
import com.eclinica.security.model.KeycloakAPIs;
import com.eclinica.security.representation.UserKeyCloackRepresentation;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
@Slf4j
public class UserKeycloakRepository {

    @Autowired
    private EclinicaSecurityConfig eclinicaSecurityConfig;

    public List<UserKeyCloackRepresentation> findByFilter(String token, UserKeyCloackRepresentation filter){
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + token);

            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);

            ResponseEntity<String> response = restTemplate.exchange(eclinicaSecurityConfig.getKeycloak().getHost() + KeycloakAPIs.USER_LIST_API, HttpMethod.GET, new HttpEntity(httpHeaders), String.class);

            if (response.getStatusCodeValue() == 200){

                    List<UserKeyCloackRepresentation> listCar = mapper.readValue(response.getBody(), new TypeReference<List<UserKeyCloackRepresentation>>(){});
                    return listCar;
            }

        } catch (JsonProcessingException e) {
            log.error("Error converting json to UserKeycloakRepresentation", e);
            return null;
        }

        return null;
    }
}
