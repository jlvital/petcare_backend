package com.petcare.model.booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.petcare.email.EmailBookingService;
import com.petcare.enums.BookingStatus;
import com.petcare.exceptions.BookingException;
import com.petcare.model.booking.dto.BookingRequest;
import com.petcare.model.booking.dto.BookingResponse;
import com.petcare.model.booking.dto.BookingUpdateRequest;
import com.petcare.model.client.Client;
import com.petcare.model.employee.Employee;
import com.petcare.model.employee.EmployeeRepository;
import com.petcare.model.pet.Pet;
import com.petcare.model.pet.PetRepository;
import com.petcare.validations.BookingValidator;
import com.petcare.validations.EmployeeValidator;
import com.petcare.validations.ClientValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PetRepository petRepository;
    private final EmployeeRepository employeeRepository;
    private final EmailBookingService emailBookingService;


    @Override
    public BookingResponse createBooking(BookingRequest request, Client client) {
        ClientValidator.validateAuthenticatedClient(client);

        Pet pet = petRepository.findById(request.getPetId()).orElse(null);
        if (pet == null) {
            log.warn("No se encontró mascota con ID: {}", request.getPetId());
            throw new BookingException("La mascota seleccionada no existe. Verifica los datos e inténtalo de nuevo.");
        }

        if (pet.getClient() == null || !pet.getClient().getId().equals(client.getId())) {
            log.warn("Mascota ID {} no pertenece al cliente ID {}", pet.getId(), client.getId());
            throw new BookingException("No puedes agendar una cita para una mascota que no está asociada a tu perfil.");
        }

        Employee employee = employeeRepository.findById(request.getEmployeeId()).orElse(null);
        if (employee == null) {
            log.warn("Profesional no encontrado con ID: {}", request.getEmployeeId());
            throw new BookingException("El profesional indicado no está disponible. Elige otro profesional.");
        }

        EmployeeValidator.validateEmployee(request.getType(), employee);
        BookingValidator.validateAvailability(bookingRepository, employee.getId(), request.getDate(), request.getTime());
        BookingValidator.validateDateAndTime(request.getDate(), request.getTime());
        BookingValidator.validateReminderSettings(request.getReminderRequest(), client);

        Booking booking = new Booking();
        booking.setPet(pet);
        booking.setEmployee(employee);
        booking.setDate(request.getDate());
        booking.setTime(request.getTime());
        booking.setType(request.getType());
        booking.setStatus(BookingStatus.CONFIRMADA);
        booking.setReminderRequest(request.getReminderRequest());
        booking.setReminderSent(false);

        Booking saved = bookingRepository.save(booking);

        emailBookingService.sendBookingAssigned(
            employee.getUsername(),
            client.getName(),
            pet.getName(),
            request.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
            request.getTime().format(DateTimeFormatter.ofPattern("HH:mm")),
            request.getType().name()
        );

        log.info("Cita creada y confirmada: ID {} | Mascota ID {} | Cliente ID {}",
                 saved.getId(), pet.getId(), client.getId());

        return toResponse(saved);
    }

    @Override
    public BookingResponse updateBooking(Long bookingId, Long clientId, BookingUpdateRequest request) {
        if (bookingId == null || bookingId <= 0 || clientId == null || clientId <= 0) {
            log.warn("Parámetros inválidos para actualizar cita. Booking ID: {}, Cliente ID: {}", bookingId, clientId);
            throw new BookingException("Datos inválidos para actualizar la cita.");
        }

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> {
                    log.warn("No se encontró la cita con ID: {}", bookingId);
                    return new BookingException("La cita no existe.");
                });

        if (booking.getPet().getClient() == null || !booking.getPet().getClient().getId().equals(clientId)) {
            log.warn("Cliente ID {} intentó modificar una cita ajena (pertenece a: {}).",
                    clientId,
                    booking.getPet().getClient() != null ? booking.getPet().getClient().getId() : "Desconocido");
            throw new BookingException("No tienes permiso para modificar esta cita.");
        }

        Employee newEmployee = employeeRepository.findById(request.getNewEmployeeId())
                .orElseThrow(() -> {
                    log.warn("Profesional no encontrado con ID: {}", request.getNewEmployeeId());
                    return new BookingException("El profesional indicado no existe.");
                });

        EmployeeValidator.validateEmployee(request.getNewType(), newEmployee);
        BookingValidator.validateAvailability(bookingRepository, newEmployee.getId(), request.getNewDate(), request.getNewTime());
        BookingValidator.validateDateAndTime(request.getNewDate(), request.getNewTime());
        BookingValidator.validateReminderSettings(request.getReminderRequest(), booking.getPet().getClient());

        booking.setDate(request.getNewDate());
        booking.setTime(request.getNewTime());
        booking.setType(request.getNewType());
        booking.setEmployee(newEmployee);
        booking.setReminderRequest(request.getReminderRequest());
        booking.setReminderSent(false);

        Booking updated = bookingRepository.save(booking);
        log.info("Cita ID {} actualizada correctamente por cliente ID {}", bookingId, clientId);
        return toResponse(updated);
    }

    @Override
    public List<BookingResponse> getBookingsByPet(Long petId) {
        List<Booking> bookings = bookingRepository.findByPetId(petId);
        List<BookingResponse> responses = new ArrayList<>();
        for (Booking booking : bookings) {
            responses.add(toResponse(booking));
        }
        return responses;
    }

    @Override
    public List<BookingResponse> getBookingsByEmployee(Long employeeId) {
        List<Booking> bookings = bookingRepository.findByEmployeeId(employeeId);
        List<BookingResponse> responses = new ArrayList<>();
        for (Booking booking : bookings) {
            responses.add(toResponse(booking));
        }
        return responses;
    }

    @Override
    public List<BookingResponse> getBookingsByClient(Long clientId) {
        List<BookingResponse> responses = new ArrayList<>();
        for (Booking booking : bookingRepository.findAll()) {
            if (booking.getPet() != null && booking.getPet().getClient() != null &&
                booking.getPet().getClient().getId().equals(clientId)) {
                responses.add(toResponse(booking));
            }
        }
        return responses;
    }

    @Override
    public List<BookingResponse> getBookingHistoryByPet(Long petId) {
        List<BookingResponse> responses = new ArrayList<>();
        for (Booking booking : bookingRepository.findByPetId(petId)) {
            if (booking.getStatus() == BookingStatus.COMPLETADA || booking.getDate().isBefore(LocalDate.now())) {
                responses.add(toResponse(booking));
            }
        }
        return responses;
    }

    @Override
    public List<LocalTime> getOccupiedHoursByEmployeeAndDate(Long employeeId, LocalDate date) {
        List<LocalTime> busyTimes = new ArrayList<>();
        for (Booking booking : bookingRepository.findByEmployeeIdAndDate(employeeId, date)) {
            busyTimes.add(booking.getTime());
        }
        return busyTimes;
    }

    @Override
    public boolean updateStatus(Long bookingId, BookingStatus newStatus) {
        if (bookingId == null || bookingId <= 0) {
            log.warn("ID de cita inválido: {}", bookingId);
            return false;
        }

        Optional<Booking> optional = bookingRepository.findById(bookingId);
        if (optional.isEmpty()) {
            log.warn("No se encontró la cita con ID: {}", bookingId);
            return false;
        }

        Booking booking = optional.get();
        booking.setStatus(newStatus);
        bookingRepository.save(booking);
        log.info("Estado actualizado a [{}] para la cita ID {}", newStatus, bookingId);
        return true;
    }

    @Override
    public boolean updateStatus(Long bookingId, BookingStatus newStatus, Long clientId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isEmpty()) {
            log.warn("No se encontró la cita con ID: {}", bookingId);
            return false;
        }

        Booking booking = bookingOpt.get();
        if (booking.getPet().getClient() == null || !booking.getPet().getClient().getId().equals(clientId)) {
            log.warn("Cliente ID {} intentó actualizar estado de una cita que no le pertenece (dueño: {}).",
                    clientId,
                    booking.getPet().getClient() != null ? booking.getPet().getClient().getId() : "N/A");
            return false;
        }

        booking.setStatus(newStatus);
        bookingRepository.save(booking);
        log.info("Cliente ID {} actualizó el estado de la cita ID {} a [{}]", clientId, bookingId, newStatus);
        return true;
    }

    private BookingResponse toResponse(Booking booking) {
        BookingResponse response = BookingResponse.builder()
            .id(booking.getId())
            .date(booking.getDate())
            .time(booking.getTime())
            .status(booking.getStatus())
            .type(booking.getType())
            .petName(booking.getPet() != null ? booking.getPet().getName() : "Desconocido")
            .employeeName(booking.getEmployee() != null ? booking.getEmployee().getName() : "Sin asignar")
            .petId(booking.getPet() != null ? booking.getPet().getId() : null)
            .employeeId(booking.getEmployee() != null ? booking.getEmployee().getId() : null)
            .durationMinutes(30)
            .employeeProfile(booking.getEmployee() != null && booking.getEmployee().getProfile() != null
                             ? booking.getEmployee().getProfile().name() : "N/A")
            .build();
        return response;
    }
}