package com.petcare.domain.booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.petcare.domain.booking.dto.*;
import com.petcare.domain.client.Client;
import com.petcare.domain.employee.Employee;
import com.petcare.domain.employee.EmployeeRepository;
import com.petcare.domain.pet.Pet;
import com.petcare.domain.pet.PetRepository;
import com.petcare.enums.BookingStatus;
import com.petcare.enums.BookingType;
import com.petcare.exceptions.*;
import com.petcare.notification.BookingEmailService;
import com.petcare.validators.AccountValidator;
import com.petcare.validators.BookingValidatorRules;
import com.petcare.validators.BookingValidator;
import com.petcare.validators.ClientValidator;
import com.petcare.validators.PetValidator;
import static com.petcare.utils.constants.GlobalConstants.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación de la lógica de negocio para la gestión de citas veterinarias.
 * <p>
 * Aquí se incluyen todas las validaciones necesarias, la creación de citas,
 * su actualización, cambios de estado y consultas personalizadas por cliente, empleado o mascota.
 * <p>
 * También se encarga del envío de correos de notificación al empleado asignado tras registrar una cita.
 *
 * @see BookingService
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PetRepository petRepository;
    private final EmployeeRepository employeeRepository;
    private final BookingEmailService bookingEmailService;

    // ╔════════════════════════════════════════════════════════════╗
    // ║          REGISTRO Y ACTUALIZACIÓN DE CITAS                ║
    // ╚════════════════════════════════════════════════════════════╝

    /**
     * Crea una nueva cita asociada a un cliente y su mascota.
     * <p>
     * Se realizan varias validaciones:
     * - Que el cliente esté activo y autenticado
     * - Que la mascota exista y le pertenezca
     * - Que el empleado exista
     * - Que el tipo de cita sea compatible con el perfil del empleado
     * - Que el horario esté dentro del rango laboral y libre
     * - Que se respeten las normas para el envío de recordatorios
     * <p>
     * Si todo es correcto, se guarda la cita en la base de datos y se envía una notificación al empleado.
     *
     * @param request Datos de la cita (fecha, hora, tipo, etc.)
     * @param client Cliente que solicita la cita.
     * @return Cita registrada con todos sus datos.
     * @throws BookingException si alguna validación falla.
     */
    @Override
    public BookingResponse createBooking(BookingRequest request, Client client) {
        ClientValidator.validateAuthenticatedClient(client);
        AccountValidator.validateAccountIsActive(client);
        
        Optional<Pet> optionalPet = petRepository.findById(request.getPetId());
        if (optionalPet.isEmpty()) {
            log.warn("No se encontró mascota con ID: {}", request.getPetId());
            throw new NotFoundException("La mascota seleccionada no existe. Verifica los datos e inténtalo de nuevo.");
        }

        Pet pet = optionalPet.get();
        PetValidator.checkOwner(pet, client.getId());

        Optional<Employee> optionalEmployee = employeeRepository.findById(request.getEmployeeId());
        if (optionalEmployee.isEmpty()) {
            log.warn("Empleado no encontrado con ID: {}", request.getEmployeeId());
            throw new NotFoundException("El empleado seleccionado no está disponible.");
        }

        Employee employee = optionalEmployee.get();

        try {
            BookingValidator.validateEmployeeProfile(request.getType(), employee.getProfile());
            BookingValidator.validateDateAndTime(request.getDate(), request.getTime());
            BookingValidator.validateAvailability(bookingRepository, employee.getId(), request.getDate(), request.getTime());
            BookingValidator.validateReminderSettings(request.getReminderRequest(), client);
        } catch (Exception e) {
            throw new BookingException("Error al validar la información de la cita: " + e.getMessage());
        }

        Booking booking = BookingMapper.toEntity(request, pet, employee);
        Booking saved = bookingRepository.save(booking);

        bookingEmailService.sendBookingAssigned(
            employee.getUsername(),
            client.getName(),
            pet.getName(),
            request.getDate().format(DATE_FORMATTER),
            request.getTime().format(TIME_FORMATTER),
            request.getType().name()
        );

        log.info("Cita creada y confirmada: ID {} | Mascota ID {} | Cliente ID {}", saved.getId(), pet.getId(), client.getId());
        return BookingMapper.toResponse(saved);
    }

    /**
     * Actualiza parcialmente una cita existente solicitada por un cliente.
     * <p>
     * Este método permite modificar algunos campos como la fecha, hora, tipo de cita,
     * solicitud de recordatorio y el profesional asignado.
     * <p>
     * Validaciones que se aplican:
     * - Que los IDs del cliente y de la cita sean válidos
     * - Que la cita exista y pertenezca al cliente
     * - Que la cuenta del cliente esté activa
     * - Que la nueva fecha/hora sean válidas (si se modifican)
     * - Que el recordatorio esté bien configurado (si se solicita)
     * - Que el nuevo empleado exista, tenga perfil compatible y esté libre (si se cambia)
     *
     * @param bookingId ID de la cita a modificar.
     * @param clientId ID del cliente autenticado.
     * @param request Objeto con los nuevos datos.
     * @return La cita modificada.
     * @throws BookingException si los datos son inválidos o no se cumple alguna regla de negocio.
     */
    
    @Override
    public BookingResponse updateBooking(Long bookingId, Long clientId, BookingUpdate request) {

        if (bookingId == null || bookingId <= 0 || clientId == null || clientId <= 0) {
            log.warn("Parámetros inválidos para actualizar cita. Booking ID: {}, Cliente ID: {}", bookingId, clientId);
            throw new BusinessException("Datos inválidos para actualizar la cita.");
        }

        Optional<Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isEmpty()) {
            log.warn("No se encontró la cita con ID: {}", bookingId);
            throw new NotFoundException("La cita no existe.");
        }

        Booking booking = optionalBooking.get();

        PetValidator.checkOwner(booking.getPet(), clientId);
        AccountValidator.validateAccountIsActive(booking.getPet().getClient());

        LocalDate nuevaFecha = request.getNewDate() != null ? request.getNewDate() : booking.getDate();
        LocalTime nuevaHora = request.getNewTime() != null ? request.getNewTime() : booking.getTime();

        try {
            if (request.getNewDate() != null || request.getNewTime() != null) {
                BookingValidator.validateDateAndTime(nuevaFecha, nuevaHora);
            }

            if (request.getReminderRequest() != null) {
                BookingValidator.validateReminderSettings(request.getReminderRequest(), booking.getPet().getClient());
            }

            if (request.getNewEmployeeId() != null) {
                Optional<Employee> optionalEmployee = employeeRepository.findById(request.getNewEmployeeId());
                if (optionalEmployee.isEmpty()) {
                    log.warn("Profesional no encontrado con ID: {}", request.getNewEmployeeId());
                    throw new NotFoundException("El empleado indicado no existe en nuestros sistemas.");
                }

                Employee newEmployee = optionalEmployee.get();
                BookingType tipo = request.getNewType() != null ? request.getNewType() : booking.getType();

                BookingValidator.validateEmployeeProfile(tipo, newEmployee.getProfile());
                BookingValidator.validateAvailability(bookingRepository, newEmployee.getId(), nuevaFecha, nuevaHora);

                booking.setEmployee(newEmployee);
            }

        } catch (Exception e) {
            throw new BookingException("Error al validar los nuevos datos de la cita: " + e.getMessage());
        }

        BookingMapper.updateEntityFromRequest(request, booking);
        Booking updated = bookingRepository.save(booking);

        log.info("Cita ID {} actualizada correctamente por cliente ID {}", bookingId, clientId);
        return BookingMapper.toResponse(updated);
    }

    /**
     * Actualiza el estado de una cita (por ejemplo: confirmada, anulada, completada).
     * <p>
     * Validaciones que se aplican:
     * - Que el ID de la cita sea válido
     * - Que la cita exista en el sistema
     * - Que el cambio de estado sea coherente según el estado actual
     * <p>
     * Si todo es correcto, se actualiza el estado y se guarda en la base de datos.
     *
     * @param bookingId ID de la cita a modificar.
     * @param newStatus Nuevo estado que se quiere aplicar.
     * @return true si el cambio se realizó correctamente.
     * @throws BookingException si los datos no son válidos o el cambio no está permitido.
     */
    
    @Override
    public boolean updateStatus(Long bookingId, BookingStatus newStatus) {
        if (bookingId == null || bookingId <= 0) {
            throw new BookingException("El ID de la cita es inválido.");
        }

        Optional<Booking> optional = bookingRepository.findById(bookingId);
        if (optional.isEmpty()) {
            throw new NotFoundException("La cita no existe.");
        }

        Booking booking = optional.get();
        BookingValidatorRules.validateStatus(booking, newStatus);

        booking.setStatus(newStatus);
        bookingRepository.save(booking);
        log.info("Estado actualizado a [{}] para la cita ID {}", newStatus, bookingId);
        return true;
    }

    /**
     * Actualiza el estado de una cita, validando además que pertenezca al cliente autenticado.
     * <p>
     * Validaciones que se aplican:
     * - Que el ID de la cita y del cliente sean válidos
     * - Que la cita exista
     * - Que la cita pertenezca al cliente autenticado
     * - Que la cuenta del cliente esté activa
     * - Que el nuevo estado sea válido respecto al estado actual
     * <p>
     * Si se cumplen todas las validaciones, se guarda el nuevo estado en base de datos.
     *
     * @param bookingId ID de la cita a modificar.
     * @param newStatus Nuevo estado deseado.
     * @param clientId ID del cliente que solicita el cambio.
     * @return true si la operación fue exitosa.
     * @throws BookingException si alguno de los datos no es válido o el cambio no está permitido.
     */
    
    @Override
    public boolean updateStatus(Long bookingId, BookingStatus newStatus, Long clientId) {
        if (bookingId == null || bookingId <= 0 || clientId == null || clientId <= 0) {
            throw new BookingException("Datos inválidos para actualizar la cita.");
        }

        Optional<Booking> optional = bookingRepository.findById(bookingId);
        if (optional.isEmpty()) {
            throw new NotFoundException("La cita no existe.");
        }

        Booking booking = optional.get();
        BookingValidatorRules.validateClientOwnership(booking, clientId);
        AccountValidator.validateAccountIsActive(booking.getPet().getClient());

        BookingValidatorRules.validateStatus(booking, newStatus);

        booking.setStatus(newStatus);
        bookingRepository.save(booking);
        log.info("Estado actualizado a [{}] para la cita ID {}", newStatus, bookingId);
        return true;
    }

    // ╔════════════════════════════════════════════════════════════╗
    // ║               CONSULTAS PARA CLIENTES                      ║
    // ╚════════════════════════════════════════════════════════════╝

    /**
     * Devuelve todas las citas registradas por un cliente, tanto pasadas como futuras.
     * <p>
     * Esta consulta se basa en el ID del cliente y recupera todas las citas asociadas a cualquiera
     * de sus mascotas. No aplica ningún filtro adicional.
     *
     * @param clientId ID del cliente autenticado.
     * @return Lista completa de citas asociadas al cliente.
     */

    @Override
    public List<BookingResponse> getBookingsByClient(Long clientId) {
        List<Booking> bookings = bookingRepository.findByPetClientId(clientId);
        return BookingMapper.toResponseList(bookings);
    }

    /**
     * Devuelve únicamente las próximas citas confirmadas que tiene programadas un cliente.
     * <p>
     * Se consideran próximas aquellas citas que:
     * - Están en estado CONFIRMADA
     * - Tienen una fecha posterior a la actual o igual al día actual con una hora futura
     * <p>
     * Esta lógica se define directamente en la consulta personalizada del repositorio.
     *
     * @param clientId ID del cliente autenticado.
     * @return Lista de citas futuras, o lista vacía si no hay ninguna.
     */

    @Override
    public List<BookingResponse> getUpcomingBookingsByClient(Long clientId) {
        List<Booking> upcomingBookings = bookingRepository.findUpcomingByClientId(clientId);
        return BookingMapper.toResponseList(upcomingBookings);
    }

    /**
     * Devuelve las citas anteriores de un cliente, es decir, aquellas que ya han finalizado.
     * <p>
     * Se consideran citas pasadas si su estado es:
     * - CANCELADA
     * - ANULADA
     * - COMPLETADA
     * <p>
     * Este filtro se aplica sobre las citas recuperadas por el ID del cliente.
     *
     * @param clientId ID del cliente autenticado.
     * @return Lista de citas finalizadas, o lista vacía si no hay ninguna.
     */

    @Override
    public List<BookingResponse> getPastBookingsByClient(Long clientId) {
        List<Booking> all = bookingRepository.findByPetClientId(clientId);
        List<Booking> filtered = new ArrayList<>();
        for (Booking booking : all) {
            if (booking.getStatus() != null && booking.getStatus().isFinal()) {
                filtered.add(booking);
            }
        }
        return BookingMapper.toResponseList(filtered);
    }

    // ╔════════════════════════════════════════════════════════════╗
    // ║               CONSULTAS PARA EMPLEADOS                    ║
    // ╚════════════════════════════════════════════════════════════╝
    
    /**
     * Devuelve todas las citas asignadas a un empleado concreto.
     * <p>
     * Esta consulta incluye tanto las citas pasadas como las futuras, sin aplicar ningún filtro adicional.
     * Se utiliza principalmente para mostrar al profesional su historial completo de atención.
     *
     * @param employeeId ID del empleado autenticado.
     * @return Lista de todas las citas asociadas al empleado.
     */
    @Override
    public List<BookingResponse> getBookingsByEmployee(Long employeeId) {
        List<Booking> bookings = bookingRepository.findByEmployeeId(employeeId);
        return BookingMapper.toResponseList(bookings);
    }

    /**
     * Devuelve únicamente las próximas citas confirmadas de un empleado.
     * <p>
     * Se consideran próximas aquellas que:
     * - Están en estado CONFIRMADA
     * - Tienen una fecha posterior a la actual, o igual al día actual con una hora futura
     * <p>
     * Esta lógica está implementada directamente en la consulta del repositorio.
     *
     * @param employeeId ID del empleado autenticado.
     * @return Lista de citas futuras, o lista vacía si no hay ninguna.
     */
    
    @Override
    public List<BookingResponse> getUpcomingBookingsByEmployee(Long employeeId) {
        List<Booking> upcoming = bookingRepository.findUpcomingByEmployeeId(employeeId);
        return BookingMapper.toResponseList(upcoming);
    }

    // ╔════════════════════════════════════════════════════════════╗
    // ║               CONSULTAS POR MASCOTA                        ║
    // ╚════════════════════════════════════════════════════════════╝

    /**
     * Devuelve todas las citas asociadas a una mascota concreta.
     * <p>
     * Se recuperan todas las citas independientemente de su estado o fecha.
     * Esta consulta se usa principalmente para mostrar el historial completo de una mascota.
     *
     * @param petId ID de la mascota.
     * @return Lista de todas las citas que ha tenido la mascota.
     */
    
    @Override
    public List<BookingResponse> getBookingsByPet(Long petId) {
        List<Booking> bookings = bookingRepository.findByPetId(petId);
        return BookingMapper.toResponseList(bookings);
    }

    /**
     * Devuelve el historial relevante de una mascota, mostrando solo citas pasadas o completadas.
     * <p>
     * Se incluyen:
     * - Citas con estado COMPLETADA
     * - Citas cuya fecha ya ha pasado (aunque no se hayan marcado como completadas)
     * <p>
     * Esta lógica permite mostrar al cliente un resumen de atención reciente de su mascota.
     *
     * @param petId ID de la mascota.
     * @return Lista de citas pasadas o completadas.
     */
    
    @Override
    public List<BookingResponse> getBookingHistoryByPet(Long petId) {
        List<BookingResponse> responses = new ArrayList<>();
        for (Booking booking : bookingRepository.findByPetId(petId)) {
            if (booking.getStatus() == BookingStatus.COMPLETADA || booking.getDate().isBefore(LocalDate.now())) {
                responses.add(BookingMapper.toResponse(booking));
            }
        }
        return responses;
    }

    // ╔════════════════════════════════════════════════════════════╗
    // ║             CONSULTA DE HORARIOS OCUPADOS                  ║
    // ╚════════════════════════════════════════════════════════════╝

    /**
     * Devuelve una lista con todas las horas ocupadas por un empleado en una fecha concreta.
     * <p>
     * Esta información se usa principalmente para comprobar disponibilidad en la creación o edición de citas.
     * Solo se tienen en cuenta las citas ya registradas (independientemente de su estado).
     *
     * @param employeeId ID del empleado.
     * @param date Fecha para la que se desea consultar disponibilidad.
     * @return Lista de horas ya ocupadas ese día por el empleado.
     */
    
    @Override
    public List<LocalTime> getOccupiedHoursByEmployeeAndDate(Long employeeId, LocalDate date) {
        List<LocalTime> busyTimes = new ArrayList<>();
        for (Booking booking : bookingRepository.findByEmployeeIdAndDate(employeeId, date)) {
            busyTimes.add(booking.getTime());
        }
        return busyTimes;
    }

    // ╔════════════════════════════════════════════════════════════╗
    // ║             CONSULTA GLOBAL PARA ADMINISTRADOR             ║
    // ╚════════════════════════════════════════════════════════════╝

    /**
     * Devuelve todas las citas registradas en el sistema, sin aplicar filtros.
     * <p>
     * Este método se reserva para el administrador del sistema, que necesita una visión global.
     * Incluye tanto citas futuras como históricas, de todos los clientes y empleados.
     *
     * @return Lista completa de todas las citas del sistema.
     */

    @Override
    public List<BookingResponse> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return BookingMapper.toResponseList(bookings);
    }
}