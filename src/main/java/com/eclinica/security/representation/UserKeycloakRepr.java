package com.eclinica.security.representation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserKeycloakRepr {
    private String  id;
    private Date    createdTimestamp;
    private String  username;
    private Boolean enabled;
    private String  firstName;
    private String  email;
    private Boolean emailVerified;
}
