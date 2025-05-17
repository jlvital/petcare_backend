package com.petcare.model.pet;

import java.time.LocalDate;
import java.util.*;

import com.petcare.config.Auditable;
import com.petcare.enums.PetGender;
import com.petcare.model.appointment.Appointment;
import com.petcare.model.client.Client;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "mascotas")
public class Pet extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(name = "nombre", nullable = false)
    private String name;

    @NotNull(message = "El sexo es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(name = "sexo", nullable = false)
    private PetGender petGender;

    @Column(name = "numero_chip")
    private String chipNumber;

    @Column(name = "edad")
    private Integer age;

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

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments = new ArrayList<>();
}
