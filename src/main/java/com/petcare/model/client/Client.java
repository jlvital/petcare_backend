package com.petcare.model.client;

import java.util.ArrayList;
import java.util.List;

import com.petcare.enums.NotificationStatus;
import com.petcare.model.pet.Pet;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "notificaciones", nullable = false)
    private NotificationStatus notificationStatus = NotificationStatus.DESACTIVADAS;
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();
}