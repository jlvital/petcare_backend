package com.petcare.model.client;

import com.petcare.model.user.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "clientes")
public class Client extends User {

    private static final long serialVersionUID = 1L;

    @Column(name = "foto_perfil")
    private String profilePictureUrl;

    @Column(name = "direccion")
    private String address;

    @Column(name = "telefono")
    private String phoneNumber;
}