package com.petcare.domain.client;

import java.util.ArrayList;
import java.util.List;

import com.petcare.domain.pet.Pet;
import com.petcare.domain.purchase.Purchase;
import com.petcare.domain.user.User;
import com.petcare.enums.NotificationStatus;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)

public class Client extends User {

    private static final long serialVersionUID = 1L;

    @Column(name = "foto_perfil")
    private String profilePictureUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "notificaciones", nullable = false)
    private NotificationStatus notificationStatus = NotificationStatus.DESACTIVADAS;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pet> pets = new ArrayList<>();
    
    @OneToMany(mappedBy = "client", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Purchase> purchases;
}