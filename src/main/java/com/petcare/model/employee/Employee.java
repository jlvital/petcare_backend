package com.petcare.model.employee;

import java.time.LocalDate;

import com.petcare.enums.Profile;
import com.petcare.model.user.User;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "empleados")
public class Employee extends User {

    private static final long serialVersionUID = 1L;

    @Column(name = "perfil", nullable = false)
    @Enumerated(EnumType.STRING)
    private Profile profile;

    @Column(name = "fecha_alta", nullable = false, updatable = false)
    private LocalDate startDate = LocalDate.now();

    @Column(name = "fecha_fin")
    private LocalDate endDate;

    @Column(name = "telefono")
    private String phoneNumber;
}