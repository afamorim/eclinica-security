package com.eclinica.security.repositry;

import com.eclinica.security.representation.UserKeycloakRepr;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Repository
@Slf4j
public class UserKeycloakRepository {

    public List<UserKeycloakRepr> findByFilter(String token, UserKeycloakRepr filter){
        try {
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add("Authorization", "Bearer " + token);

            JsonFactory factory = new JsonFactory();
            ObjectMapper mapper = new ObjectMapper(factory);

            ResponseEntity<String> response = restTemplate.exchange("http://localhost:8080/auth/admin/realms/E-Clinica/users", HttpMethod.GET, new HttpEntity(httpHeaders), String.class);

            if (response.getStatusCodeValue() == 200){

                    List<UserKeycloakRepr> listCar = mapper.readValue(response.getBody(), new TypeReference<List<UserKeycloakRepr>>(){});
                    return listCar;
            }

        } catch (JsonProcessingException e) {
            log.error("Error converting json to UserKeycloakRepresentation", e);
            return null;
        }

        return null;
    }
}
