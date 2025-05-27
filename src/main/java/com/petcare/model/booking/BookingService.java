package com.petcare.model.booking;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.petcare.enums.BookingStatus;
import com.petcare.model.booking.dto.*;
import com.petcare.model.client.Client;

public interface BookingService {

	BookingResponse createBooking(BookingRequest request, Client client);

	BookingResponse updateBooking(Long bookingId, Long clientId, BookingUpdateRequest request);

	boolean updateStatus(Long id, BookingStatus newStatus);

	boolean updateStatus(Long bookingId, BookingStatus newStatus, Long clientId);

	List<BookingResponse> getBookingsByPet(Long petId);

	List<BookingResponse> getBookingsByClient(Long clientId);

	List<BookingResponse> getBookingsByEmployee(Long employeeId);

	List<BookingResponse> getBookingHistoryByPet(Long petId);

	List<LocalTime> getOccupiedHoursByEmployeeAndDate(Long employeeId, LocalDate date);
}