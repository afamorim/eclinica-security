package com.eclinica.security.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String  cpf;
}
