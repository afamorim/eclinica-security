package com.eclinica.security.model;

public enum KeycloakAPIs {

    TOKEN_API("/auth/realms/E-Clinica/protocol/openid-connect/token"), USER_LIST_API("/auth/admin/realms/E-Clinica/users");

    private final String value;

    KeycloakAPIs(String aValue){
        this.value = aValue;
    }

    public String getValue(){
        return this.value;
    }
}
