package com.eclinica.security.service;

import com.eclinica.security.repositry.AuthRepository;
import com.eclinica.security.repositry.UserKeycloakRepository;
import com.eclinica.security.representation.UserKeycloakRepr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UsuarioService {

    @Autowired
    private AuthRepository authRepository;

    @Autowired
    private UserKeycloakRepository userKeycloakRepository;

    public List<UserKeycloakRepr> findByFilter(UserKeycloakRepr filter){
        log.info("chamando o authRepository");

        List userList =  userKeycloakRepository.findByFilter(authRepository.getToken(), filter);

        log.info((userList != null) ? userList.toString() : "Null");

        return userList;
    }
}
