package com.eclinica.security.representation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserKeyCloackRepresentation {
    private String  id;
    private Date    createdTimestamp;
    private String  username;
    private Boolean enabled;
    private String  firstName;
    private String  email;
    private Boolean emailVerified;
}
