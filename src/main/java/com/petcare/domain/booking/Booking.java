package com.petcare.domain.booking;

import java.time.LocalDate;
import java.time.LocalTime;

import com.petcare.config.Auditable;
import com.petcare.domain.employee.Employee;
import com.petcare.domain.pet.Pet;
import com.petcare.enums.BookingStatus;
import com.petcare.enums.BookingType;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entidad JPA que representa una cita veterinaria entre un cliente (y su mascota) y un empleado.
 * <p>
 * Cada cita contiene información sobre la fecha, hora, tipo de servicio solicitado,
 * si el cliente ha pedido recibir un recordatorio, y su estado actual (confirmada, cancelada, etc).
 * <p>
 * También se establece la relación con la mascota y con el empleado asignado a la cita.
 *
 * @see com.petcare.enums.BookingType
 * @see com.petcare.enums.BookingStatus
 * @see com.petcare.domain.pet.Pet
 * @see com.petcare.domain.employee.Employee
 */

@Entity
@Table(name = "citas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking extends Auditable {

    // ╔════════════════════════════════════════════════════╗
    // ║                 IDENTIFICADOR PRINCIPAL            ║
    // ╚════════════════════════════════════════════════════╝
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cita")
    private Long id;

    // ╔════════════════════════════════════════════════════╗
    // ║             FECHA Y HORA DE LA CITA                ║
    // ╚════════════════════════════════════════════════════╝
    
    @Column(name = "fecha", nullable = false)
    private LocalDate date;

    @Column(name = "hora", nullable = false)
    private LocalTime time;

    // ╔════════════════════════════════════════════════════╗
    // ║              RECORDATORIOS Y ESTADO                ║
    // ╚════════════════════════════════════════════════════╝
    
    @Column(name = "recordatorio", nullable = false)
    private Boolean reminderRequest = false;

    @Column(name = "notificado", nullable = false)
    private Boolean reminderSent = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private BookingStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false)
    private BookingType type;

    // ╔════════════════════════════════════════════════════╗
    // ║        RELACIONES: MASCOTA Y PROFESIONAL           ║
    // ╚════════════════════════════════════════════════════╝
    
    @ManyToOne
    @JoinColumn(name = "id_mascota", nullable = false)
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "id_empleado", nullable = false)
    private Employee employee;
}