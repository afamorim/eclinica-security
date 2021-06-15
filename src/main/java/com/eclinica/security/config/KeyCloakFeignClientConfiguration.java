package com.eclinica.security.config;

import com.eclinica.security.model.KeycloakAPIs;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class KeyCloakFeignClientConfiguration {

    @Autowired
    private OAuth2AuthorizedClientManager   authorizedClientManager;

    @Autowired
    private EclinicaSecurityConfig  eclinicaSecurityConfig;

    private final String ACCESS_TOKEN = "access_token";

    @Bean
    public RequestInterceptor requestInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
//                if (template.headers().get("Authorization") != null) {
                    OAuth2AccessToken accessToken = getAccessToken();
                    template.header("Authorization", "Bearer " + getToken());
//                }
            }
        };
    }

    private OAuth2AccessToken getAccessToken() {
        OAuth2AuthorizeRequest request = OAuth2AuthorizeRequest
                .withClientRegistrationId("keycloak") // <- Here you load your registered client
                .principal(SecurityContextHolder.getContext().getAuthentication())
                .build();
        return authorizedClientManager.authorize(request).getAccessToken();
    }

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

        log.debug("chamada para retorno do token " + eclinicaSecurityConfig.getKeycloak().getHost());

        ResponseEntity<String> response = restTemplate.postForEntity(eclinicaSecurityConfig.getKeycloak().getHost() + KeycloakAPIs.TOKEN_API.getValue(), request, String.class);

        if (response.getStatusCodeValue() == 200){
            try {
                JsonFactory factory = new JsonFactory();

                ObjectMapper mapper = new ObjectMapper(factory);

                JsonNode rootNode = mapper.readTree(response.getBody());
                Iterator<Map.Entry<String,JsonNode>> fieldsIterator = rootNode.fields();
                AtomicReference<String> token_return = new AtomicReference<String>();
                fieldsIterator.forEachRemaining(entry -> {
                    if (entry.getKey().equals(ACCESS_TOKEN)){
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

//    private OAuth2ProtectedResourceDetails resource() {
//        final ClientCredentialsResourceDetails details = new ClientCredentialsResourceDetails();
//        details.setAccessTokenUri("http://localhost:8090/security/token");
//        details.setClientId("demo");
//        details.setClientSecret("secret");
//        details.setScope(Arrays.asList("read", "write"));
//        return details;
//    }
}
