package com.eclinica.security.api;

import com.eclinica.security.representation.UserKeycloakRepr;
import com.eclinica.security.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/users/v1")
public class UserApi {

    @Autowired
    private UsuarioService userService;

    @RolesAllowed("admin")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<String> getUsers() {
        userService.findByFilter(UserKeycloakRepr.builder().build());
        return ResponseEntity.ok("Hello Admin");
    }
}
