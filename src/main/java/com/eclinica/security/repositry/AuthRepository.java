package com.eclinica.security.repositry;

import com.eclinica.security.config.EclinicaSecurityConfig;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Iterator;
import java.util.Map;

@Repository
@Slf4j
public class AuthRepository {//extends RouteBuilder {

    @Autowired
    private EclinicaSecurityConfig eclinicaSecurityConfig;

//    @Override
    public String getToken()  {
        /*restConfiguration().producerComponent("http4");

        from("timer://test?period=300000")
                .process(
                        exchange -> exchange.getIn()
                                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                                .setHeader(Exchange.CONTENT_TYPE,  constant("application/x-www-form-urlencoded"))


                );*/
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        httpHeaders.add("Authorization", "Basic: ZWNsaW5pY2Etc2VjdXJpdHk6NDI4ZjgwZTktZWFiOS00YzA4LWEwZDctY2NlYjIyMDY3YjY5");
//        httpHeaders.setBasicAuth(eclinicaSecurityConfig.getKeycloak().getClientId(), eclinicaSecurityConfig.getKeycloak().getSecret());

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, httpHeaders);

        log.info("chamada para retorno do toke");
        ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/auth/realms/E-Clinica/protocol/openid-connect/token", request, String.class);

        if (response.getStatusCodeValue() == 200){
            try {
                JsonFactory factory = new JsonFactory();

                ObjectMapper mapper = new ObjectMapper(factory);

                JsonNode rootNode = mapper.readTree(response.getBody());
                Iterator<Map.Entry<String,JsonNode>> fieldsIterator = rootNode.fields();
                while (fieldsIterator.hasNext()) {

                    Map.Entry<String,JsonNode> field = fieldsIterator.next();
                    if (field.getKey().equals("access_token")){
                        return field.getValue().asText();
                    }
                }
            } catch (JsonProcessingException e) {
                log.error("Error json deserialization", e);
            }
        }

        return null;
    }
}
