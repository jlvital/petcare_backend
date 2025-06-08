package com.petcare.domain.employee;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.petcare.domain.booking.Booking;
import com.petcare.domain.user.User;
import com.petcare.enums.Profile;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "empleados")
public class Employee extends User {

    private static final long serialVersionUID = 1L;

    @Enumerated(EnumType.STRING)
    @Column(name = "perfil", nullable = false)
    private Profile profile;

    @Column(name = "fecha_alta", nullable = false, updatable = false)
    private LocalDate startDate = LocalDate.now();

    @Column(name = "fecha_fin")
    private LocalDate endDate;
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();
}