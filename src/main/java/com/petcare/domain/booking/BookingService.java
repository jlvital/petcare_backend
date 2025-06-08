package com.petcare.domain.booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.petcare.domain.booking.dto.*;
import com.petcare.domain.client.Client;
import com.petcare.enums.BookingStatus;


/**

* Interfaz que define las operaciones de negocio relacionadas con la gestión de citas veterinarias.

* <p>
* Separa la lógica de negocio de la implementación técnica. Desde aquí se pueden registrar,
* actualizar, consultar y cancelar citas, tanto desde el punto de vista del cliente como del empleado.
* <p>
*/

public interface BookingService {

    // ╔════════════════════════════════════════════════════╗
    // ║              REGISTRO Y ACTUALIZACIÓN DE CITAS     ║
    // ╚════════════════════════════════════════════════════╝

	/**
     * Registra una nueva cita solicitada por un cliente.
     *
     * @param request Datos de la cita.
     * @param client Cliente autenticado que solicita la cita.
     * @return Datos de la cita registrada.
     */
	
    BookingResponse createBooking(BookingRequest request, Client client);

    /**
    * Permite modificar/actualizar los datos de una cita.
    *
    * @param bookingId ID de la cita a modificar.
    * @param clientId ID del cliente que solicita el cambio.
    * @param request Nuevos datos a aplicar.
    * @return Datos actualizados de la cita.
    */
    
    BookingResponse updateBooking(Long bookingId, Long clientId, BookingUpdate request);

    /**
     * Cambia el estado de una cita (por ejemplo, a COMPLETADA o ANULADA).
     *
     * @param bookingId ID de la cita.
     * @param newStatus Nuevo estado a aplicar.
     * @return true si el cambio fue exitoso.
     */
    
    boolean updateStatus(Long bookingId, BookingStatus newStatus);

    /**
     * Cambia el estado de una cita, validando además que el cliente sea su propietario.
     *
     * @param bookingId ID de la cita.
     * @param newStatus Nuevo estado a aplicar.
     * @param clientId ID del cliente que solicita el cambio.
     * @return true si el cambio fue exitoso.
     */
    
    boolean updateStatus(Long bookingId, BookingStatus newStatus, Long clientId);

    // ╔════════════════════════════════════════════════════╗
    // ║               CONSULTAS PARA CLIENTES              ║
    // ╚════════════════════════════════════════════════════╝

    /**
     * Devuelve todas las citas asociadas a un cliente.
     *
     * @param clientId ID del cliente.
     * @return Lista de citas.
     */
    
    List<BookingResponse> getBookingsByClient(Long clientId);

    /**
     * Devuelve las próximas citas programadas de un cliente.
     *
     * @param clientId ID del cliente.
     * @return Lista de citas futuras.
     */
    
    List<BookingResponse> getUpcomingBookingsByClient(Long clientId);

    /**
     * Devuelve las citas pasadas o finalizadas de un cliente.
     *
     * @param clientId ID del cliente.
     * @return Lista de citas anteriores.
     */
    
    List<BookingResponse> getPastBookingsByClient(Long clientId);

    // ╔════════════════════════════════════════════════════╗
    // ║              CONSULTAS PARA EMPLEADOS              ║
    // ╚════════════════════════════════════════════════════╝

    /**
     * Devuelve todas las citas asociadas a un empleado.
     *
     * @param employeeId ID del empleado.
     * @return Lista de citas.
     */
    
    List<BookingResponse> getBookingsByEmployee(Long employeeId);

    /**
     * Devuelve las próximas citas programadas de un empleado.
     *
     * @param employeeId ID del empleado.
     * @return Lista de citas futuras.
     */
    
    List<BookingResponse> getUpcomingBookingsByEmployee(Long employeeId);

    // ╔════════════════════════════════════════════════════╗
    // ║               CONSULTAS POR MASCOTA                ║
    // ╚════════════════════════════════════════════════════╝

    /**
     * Devuelve todas las citas registradas para una mascota concreta.
     *
     * @param petId ID de la mascota.
     * @return Lista de citas.
     */
    
    List<BookingResponse> getBookingsByPet(Long petId);

    /**
     * Devuelve las citas anteriores o completadas de una mascota.
     *
     * @param petId ID de la mascota.
     * @return Lista de citas históricas.
     */
    
    List<BookingResponse> getBookingHistoryByPet(Long petId);

    // ╔════════════════════════════════════════════════════╗
    // ║             CONSULTA DE DISPONIBILIDAD	         	║
    // ╚════════════════════════════════════════════════════╝

    /**
     * Devuelve las horas que ya están ocupadas por un empleado en un día concreto.
     *
     * @param employeeId ID del empleado.
     * @param date Día que se quiere consultar.
     * @return Lista de horas ya reservadas.
     */
    
    List<LocalTime> getOccupiedHoursByEmployeeAndDate(Long employeeId, LocalDate date);

    // ╔════════════════════════════════════════════════════╗
    // ║            CONSULTAS ADMINISTRADORAS GLOBALES      ║
    // ╚════════════════════════════════════════════════════╝
    /**
     * Devuelve todas las citas del sistema (consulta global para administradores).
     *
     * @return Lista completa de citas.
     */
    
    List<BookingResponse> getAllBookings();
}