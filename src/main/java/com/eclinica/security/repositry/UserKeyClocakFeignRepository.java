package com.eclinica.security.repositry;

import com.eclinica.security.config.KeyCloakFeignClientConfiguration;
import com.eclinica.security.representation.UserKeycloakRepr;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="keycloaks", configuration = KeyCloakFeignClientConfiguration.class, url = "http://localhost:8080/auth")
public interface UserKeyClocakFeignRepository {

    @RequestMapping(method = RequestMethod.GET, value = "/admin/realms/E-Clinica/users")
    public List<UserKeycloakRepr> findByFilter(@RequestParam("name") String name, @RequestParam("username") String uername);
}
