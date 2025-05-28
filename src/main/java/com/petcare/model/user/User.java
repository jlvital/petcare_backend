package com.petcare.model.user;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

import com.petcare.config.Auditable;
import com.petcare.enums.AccountStatus;
import com.petcare.enums.Role;
import com.petcare.enums.UserGender;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EntityListeners(org.springframework.data.jpa.domain.support.AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "usuarios")
public class User extends Auditable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false)
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @Column(name = "apellido_1")
    private String lastName1;

    @Column(name = "apellido_2")
    private String lastName2;

    @Email(message = "Formato de correo inválido")
    @Column(name = "email", nullable = false)
    private String recoveryEmail;

    @Column(name = "telefono")
    private String phoneNumber;

    @Column(name = "direccion")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "sexo")
    private UserGender gender;

    @Column(name = "fecha_nacimiento")
    private LocalDate birthDate;

    @Column(name = "usuario", unique = true, nullable = false)
    @NotBlank(message = "Debes indicar un email válido como nombre de usuario")
    @Email(message = "Formato de email no válido")
    private String username;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "Debe tener al menos 8 caracteres")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[!@#$%^&*]).+$", message = "Debe incluir al menos una mayúscula y un carácter especial")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    @NotNull(message = "El rol es obligatorio")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private AccountStatus accountStatus = AccountStatus.ACTIVA;

    @Column(name = "ultimo_acceso", nullable = false, updatable = false)
    private LocalDateTime lastAccess = LocalDateTime.now();

    @Column(name = "cambio_password")
    private LocalDateTime lastPasswordChange = LocalDateTime.now();

    @Column(name = "errores")
    private Integer failedLoginAttempts = 0;

    @Column(name = "token")
    private String recoveryToken;

    @Column(name = "expiracion")
    private LocalDateTime recoveryTokenExpiration;
}