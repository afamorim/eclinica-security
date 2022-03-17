package com.eclinica.security.repository;

import com.eclinica.security.config.KeyCloakFeignClientConfiguration;
import com.eclinica.security.representation.UserKeyCloackRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="keycloaks", configuration = KeyCloakFeignClientConfiguration.class, url = "http://localhost:8080/auth")
public interface UserKeyClocakFeignRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/admin/realms/E-Clinica/users")
    public List<UserKeyCloackRepresentation> findByFilter(@RequestParam("name") String name, @RequestParam("username") String uername);

    @RequestMapping(method = RequestMethod.POST, value = "/admin/realms/E-Clinica/users")
    public void create(@RequestBody UserKeyCloackRepresentation userKeyCloackRepresentation);
}
