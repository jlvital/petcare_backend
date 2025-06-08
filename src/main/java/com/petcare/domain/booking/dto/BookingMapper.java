package com.petcare.domain.booking.dto;

import java.util.ArrayList;
import java.util.List;

import com.petcare.domain.booking.Booking;
import com.petcare.domain.employee.Employee;
import com.petcare.domain.pet.Pet;
import com.petcare.enums.BookingStatus;
import com.petcare.enums.PetType;
import com.petcare.utils.constants.*;
import com.petcare.utils.NameFormatter;

/**
 * Clase utilitaria encargada de convertir objetos relacionados con citas veterinarias.
 * <p>
 * Permite transformar entre entidades {@link Booking} y sus representaciones DTO:
 * {@link BookingRequest}, {@link BookingUpdate} y {@link BookingResponse}.
 * <p>
 * Su uso centraliza la lógica de mapeo y evita duplicación de código en los servicios.
 * No contiene validaciones, solo construcción y adaptación de estructuras.
 */
public class BookingMapper {

    // ╔════════════════════════════════════════════════════╗
    // ║         DTO >> Entidad Booking                    ║
    // ╚════════════════════════════════════════════════════╝
    
	/**
     * Convierte los datos de una petición de cita junto a una mascota y un profesional
     * en una nueva entidad {@link Booking}.
     * <p>
     * Este método se usa durante el proceso de creación de una cita. Asigna el estado
     * por defecto a CONFIRMADA y establece que aún no se ha enviado el recordatorio.
     *
     * @param request Datos recibidos desde el formulario del cliente.
     * @param pet Mascota asociada a la cita.
     * @param employee Empleado que atenderá la cita.
     * @return Nueva instancia de Booking lista para persistir, o {@code null} si algún dato es nulo.
     */
	
	public static Booking toEntity(BookingRequest request, Pet pet, Employee employee) {
        if (request == null || pet == null || employee == null) {
            return null;
        }

        Booking booking = new Booking();
        booking.setDate(request.getDate());
        booking.setTime(request.getTime());
        booking.setType(request.getType());
        booking.setStatus(BookingStatus.CONFIRMADA);
        booking.setReminderRequest(request.getReminderRequest());
        booking.setReminderSent(false);
        booking.setPet(pet);
        booking.setEmployee(employee);

        return booking;
    }

    // ╔════════════════════════════════════════════════════╗
    // ║         Entidad >> BookingResponse                ║
    // ╚════════════════════════════════════════════════════╝
    
	 /**
     * Convierte una entidad {@link Booking} en un objeto {@link BookingResponse}
     * listo para ser enviado al frontend.
     * <p>
     * Incluye datos enriquecidos como:
     * - Nombre del cliente
     * - Nombre y tipo de mascota
     * - Nombre y perfil del profesional
     * - Etiquetas legibles del estado y tipo de cita
     *
     * @param booking Cita que se desea transformar.
     * @return DTO con todos los datos para mostrar en pantalla, o {@code null} si la cita es nula.
     */
	
	public static BookingResponse toResponse(Booking booking) {
	    if (booking == null) {
	        return null;
	    }

	    Pet pet = booking.getPet();
	    Long petId = null;
	    String petName = null;
	    String petType = null;

	    if (pet != null) {
	        petId = pet.getId();
	        petName = pet.getName();

	        if (pet.getType() == PetType.OTRO && pet.getCustomType() != null) {
	            petType = pet.getCustomType();
	        } else if (pet.getType() != null) {
	            petType = pet.getType().name();
	        }
	    }

	    String clientName = null;
	    if (pet != null && pet.getClient() != null) {
	        clientName = NameFormatter.getShortFullName(pet.getClient());
	    }

	    Employee employee = booking.getEmployee();
	    Long employeeId = null;
	    String employeeName = null;
	    String employeeProfileLabel = null; // ✅ refactor

	    if (employee != null) {
	        employeeId = employee.getId();
	        employeeName = NameFormatter.getFullName(employee);

	        if (employee.getProfile() != null) {
	            employeeProfileLabel = employee.getProfile().getLabel(); // ✅ uso correcto
	        }
	    }

	    return BookingResponse.builder()
	            .id(booking.getId())
	            .date(booking.getDate())
	            .time(booking.getTime())
	            .status(booking.getStatus())
	            .statusLabel(booking.getStatus() != null ? booking.getStatus().getLabel() : null)
	            .type(booking.getType())
	            .typeLabel(booking.getType() != null ? booking.getType().getLabel() : null)
	            .reminderRequest(booking.getReminderRequest())
	            .reminderSent(booking.getReminderSent())
	            .petId(petId)
	            .petName(petName)
	            .petType(petType)
	            .employeeId(employeeId)
	            .employeeName(employeeName)
	            .employeeProfileLabel(employeeProfileLabel)
	            .clientName(clientName)
	            .durationMinutes(GlobalConstants.BOOKING_DEFAULT_DURATION_MINUTES)
	            .build();
	}

    // ╔════════════════════════════════════════════════════╗
    // ║         Actualización desde BookingUpdateRequest   ║
    // ╚════════════════════════════════════════════════════╝
    
	/**
     * Aplica los valores informados en una petición de actualización sobre una cita existente.
     * <p>
     * Solo se modifican los campos no nulos:
     * - Fecha
     * - Hora
     * - Tipo
     * - Solicitud de recordatorio
     * <p>
     * No se actualiza el profesional asignado (eso se hace directamente en el servicio).
     *
     * @param request Objeto con los nuevos valores (parciales).
     * @param booking Entidad original que se desea modificar.
     */
	
	public static void updateEntityFromRequest(BookingUpdate request, Booking booking) {
        if (request == null || booking == null) {
            return;
        }

        if (request.getNewDate() != null) {
            booking.setDate(request.getNewDate());
        }

        if (request.getNewTime() != null) {
            booking.setTime(request.getNewTime());
        }

        if (request.getNewType() != null) {
            booking.setType(request.getNewType());
        }

        if (request.getReminderRequest() != null) {
            booking.setReminderRequest(request.getReminderRequest());
            booking.setReminderSent(false); 
        }
    }

    // ╔════════════════════════════════════════════════════╗
    // ║     Conversión de lista de Booking a ResponseList  ║
    // ╚════════════════════════════════════════════════════╝
	
	/**
     * Transforma una lista de entidades {@link Booking} en una lista de {@link BookingResponse}.
     * <p>
     * Se usa para devolver resultados al frontend, ya formateados y listos para mostrar.
     *
     * @param bookings Lista de entidades a convertir.
     * @return Lista de respuestas mapeadas, vacía si no hay resultados.
     */	
	
    public static List<BookingResponse> toResponseList(List<Booking> bookings) {
        List<BookingResponse> responses = new ArrayList<>();
        for (Booking booking : bookings) {
            responses.add(toResponse(booking));
        }
        return responses;
    }
}