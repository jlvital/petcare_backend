package com.petcare.domain.pet;

import java.time.LocalDate;
import java.util.*;

import com.petcare.config.Auditable;
import com.petcare.domain.booking.Booking;
import com.petcare.domain.client.Client;
import com.petcare.domain.report.Report;
import com.petcare.domain.treatment.Treatment;
import com.petcare.domain.vaccine.Vaccine;
import com.petcare.enums.PetGender;
import com.petcare.enums.PetType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mascotas")
public class Pet extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", nullable = false)
    private PetGender petGender;
    
    @Column(name = "genero_personalizado")
    private String customGender;

    @Column(name = "numero_chip", unique = true)
    private String chipNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private PetType type;

    @Column(name = "tipo_personalizado")
    private String customType;

    @Column(name = "raza")
    private String breed;

    @Column(name = "fecha_nacimiento")
    private LocalDate birthDate;

    @Column(name = "fecha_adopcion")
    private LocalDate adoptionDate;

    @Column(name = "peso")
    private Double weight;

    @Column(name = "esterilizado")
    private Boolean sterilized = false;

    @Column(name = "fecha_esterilizacion")
    private LocalDate sterilizationDate;

    @Column(name = "observaciones")
    private String observations;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Client client;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Vaccine> vaccines = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Report> reports = new ArrayList<>();

    @OneToMany(mappedBy = "pet", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Treatment> treatments = new ArrayList<>();
}