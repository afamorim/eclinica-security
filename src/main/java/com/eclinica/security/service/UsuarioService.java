package com.eclinica.security.service;

import com.eclinica.security.repository.AuthRepository;
import com.eclinica.security.repository.UserKeyClocakFeignRepository;
import com.eclinica.security.repository.UserKeycloakRepository;
import com.eclinica.security.representation.UserKeyCloackRepresentation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UsuarioService {

    @Autowired
    private AuthRepository                  authRepository;

    @Autowired
    private UserKeycloakRepository          userKeycloakRepository;

    @Autowired
    private UserKeyClocakFeignRepository    userKeyClocakFeignRepository;


    public List<UserKeyCloackRepresentation> findByFilter(UserKeyCloackRepresentation filter){
        log.debug("chamando o authRepository");

        //userKeycloakRepository.findByFilter(authRepository.getToken(), filter);
        List userList =  userKeyClocakFeignRepository.findByFilter(filter.getFirstName(), filter.getUsername());

        log.info((userList != null) ? userList.toString() : "Null");

        return userList;
    }
}
