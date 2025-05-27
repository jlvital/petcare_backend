package com.petcare.model.client;

import com.petcare.enums.NotificationStatus;
import com.petcare.model.user.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "clientes")
public class Client extends User {

    private static final long serialVersionUID = 1L;

    // 1. Visual
    @Column(name = "foto_perfil")
    private String profilePictureUrl;

    // 2. Preferencias
    @Enumerated(EnumType.STRING)
    @Column(name = "notificaciones", nullable = false)
    private NotificationStatus notificationStatus = NotificationStatus.DESACTIVADAS;
}
