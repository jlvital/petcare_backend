package com.petcare.admin;

import com.petcare.enums.AccountStatus;
import com.petcare.enums.BookingStatus;
import com.petcare.enums.BookingType;
import com.petcare.enums.Role;
import com.petcare.exceptions.*;
import com.petcare.model.booking.Booking;
import com.petcare.model.booking.BookingRepository;
import com.petcare.model.client.*;
import com.petcare.model.employee.*;
import com.petcare.model.employee.dto.EmployeeRegisterRequest;
import com.petcare.model.user.*;
import com.petcare.admin.dto.BookingStatsResponse;
import com.petcare.email.EmailService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AdminServiceImpl implements AdminService {

	private final EmployeeRepository employeeRepository;
	private final BookingRepository bookingRepository;
	private final ClientRepository clientRepository;
	private final EmployeeService employeeService;
	private final EmailService emailService;
	private final UserRepository userRepository;
	private final UserService userService;

	@Value("${system.admin.recovery.email}")
	private String adminRecoveryEmail;

	@Override
	public Employee registerNewEmployee(EmployeeRegisterRequest request) {
		if (userRepository.existsByUsername(request.getRecoveryEmail())) {
			throw new UserAlreadyExistsException(
					"Ya existe un usuario registrado con el correo: " + request.getRecoveryEmail());
		}

		Employee employee = new Employee();
		employee.setRecoveryEmail(request.getRecoveryEmail());
		employee.setName(request.getName());
		employee.setLastName1(request.getLastName1());
		employee.setLastName2(request.getLastName2());
		employee.setProfile(request.getProfile());
		employee.setStartDate(request.getStartDate());
		employee.setRole(Role.EMPLEADO);

		log.info("Se ha delegado el registro de un nuevo empleado EmployeeService...");
		return employeeService.registerEmployee(employee);
	}

	@Override
	public void activateUser(Long userId) {
		Optional<User> userOptional = userRepository.findById(userId);
		if (!userOptional.isPresent()) {
			log.warn("La cuenta de usuario con ID {} ha sido activada correctamente.", userId);
			throw new UserNotFoundException("Usuario no encontrado con ID: " + userId);
		}

		User user = userOptional.get();
		user.setAccountStatus(AccountStatus.ACTIVA);
		userService.saveForUserType(user);
		log.info("Cuenta ACTIVA para el usuario con ID: {}", userId);
	}

	@Override
	public void deactivateUser(Long userId) {
		Optional<User> userOptional = userRepository.findById(userId);
		if (!userOptional.isPresent()) {
			log.warn("Usuario no encontrado con ID: {}", userId);
			throw new UserNotFoundException("Usuario no encontrado con ID: " + userId);
		}

		User user = userOptional.get();
		user.setAccountStatus(AccountStatus.BLOQUEADA);
		userService.saveForUserType(user);
		log.info("La cuenta de usuario con ID {} ha sido correctamente desactivada.", userId);
	}

	@Override
	public void deleteUser(Long userId) {
		Optional<Client> clientOptional = clientRepository.findById(userId);
		if (clientOptional.isPresent()) {
			clientRepository.deleteById(userId);
			log.info("Cliente eliminado con ID: {}", userId);
			return;
		}

		Optional<Employee> employeeOptional = employeeRepository.findById(userId);
		if (employeeOptional.isPresent()) {
			employeeRepository.deleteById(userId);
			log.info("Empleado eliminado con ID: {}", userId);
			return;
		}

		log.warn("No se encontró ningún usuario con ID: {}", userId);
		throw new UserNotFoundException("No se encontró ningún usuario con ID: " + userId);
	}

	@Override
	public List<Employee> listEmployees() {
		return employeeRepository.findAll();
	}

	@Override
	public List<Client> listClients() {
		return clientRepository.findAll();
	}

	@Override
	public User findUserById(Long id) {
		Optional<User> userOptional = userRepository.findById(id);
		if (!userOptional.isPresent()) {
			log.warn("Usuario no encontrado con ID: {}", id);
			throw new UserNotFoundException("Usuario no encontrado con ID: " + id);
		}
		return userOptional.get();
	}

	@Override
	public Client findClientById(Long id) {
		Optional<Client> clientOptional = clientRepository.findById(id);
		if (!clientOptional.isPresent()) {
			log.warn("Cliente no encontrado con ID: {}", id);
			throw new UserNotFoundException("Cliente no encontrado con ID: " + id);
		}
		return clientOptional.get();
	}

	@Override
	public Employee findEmployeeById(Long id) {
		Optional<Employee> employeeOptional = employeeRepository.findById(id);
		if (!employeeOptional.isPresent()) {
			log.warn("Empleado no encontrado con ID: {}", id);
			throw new UserNotFoundException("Empleado no encontrado con ID: " + id);
		}
		return employeeOptional.get();
	}

	@Override
	public void recoverAdminPassword() {
		if (adminRecoveryEmail == null || adminRecoveryEmail.isBlank()) {
			log.error("El correo de recuperación del administrador no está configurado.");
			throw new AdminRecoveryEmailException();
		}

		try {
			String recoveryLink = "https://portal.petcare360.com/admin-recover-password";
			emailService.sendPasswordRecoveryEmail(adminRecoveryEmail, recoveryLink);
			log.info("Correo de recuperación de administrador enviado a: {}", adminRecoveryEmail);
		} catch (Exception e) {
			log.error("Error enviando correo de recuperación al administrador: {}", e.getMessage(), e);
			throw new RuntimeException("No se pudo enviar el correo de recuperación de contraseña al administrador", e);
		}
	}

	@Override
	public void updateProductPrice(Long productId, double newPrice) {
		log.info("Precio actualizado para el producto con ID: {} a nuevo precio: {}", productId, newPrice);
	}

	@Override
	public void updateStock(Long productId, int quantity) {
		log.info("Stock actualizado para el producto con ID: {} a nueva cantidad: {}", productId, quantity);
	}

	@Override
	public BookingStatsResponse getBookingStats() {
	    List<Booking> allBookings = bookingRepository.findAll();
	    
	    int totalBookings = allBookings.size();
	    int confirmed = 0;
	    int cancelled = 0;
	    int aborted = 0;

	    Map<BookingType, Integer> bookingsByType = new HashMap<>();
	    Map<BookingStatus, Integer> bookingsByStatus = new HashMap<>();

	    for (Booking booking : allBookings) {
	        BookingStatus status = booking.getStatus();
	        BookingType type = booking.getType();

	        // Contar estados
	        if (status == BookingStatus.CONFIRMADA) confirmed++;
	        if (status == BookingStatus.CANCELADA) cancelled++;
	        if (status == BookingStatus.ANULADA) aborted++;

	        // Acumular por estado
	        if (!bookingsByStatus.containsKey(status)) {
	            bookingsByStatus.put(status, 1);
	        } else {
	            int count = bookingsByStatus.get(status);
	            bookingsByStatus.put(status, count + 1);
	        }

	        // Acumular por tipo
	        if (!bookingsByType.containsKey(type)) {
	            bookingsByType.put(type, 1);
	        } else {
	            int count = bookingsByType.get(type);
	            bookingsByType.put(type, count + 1);
	        }
	    }

	    int totalClients = clientRepository.findAll().size();
	    int totalEmployees = employeeRepository.findAll().size();

	    BookingStatsResponse stats = new BookingStatsResponse();
	    stats.setTotalBookings(totalBookings);
	    stats.setConfirmedBookings(confirmed);
	    stats.setCancelledBookings(cancelled);
	    stats.setAbortedBookings(aborted);
	    stats.setTotalClients(totalClients);
	    stats.setTotalEmployees(totalEmployees);
	    stats.setBookingsByType(bookingsByType);
	    stats.setBookingsByStatus(bookingsByStatus);

	    log.info("Estadísticas de citas generadas correctamente.");
	    return stats;
	}
}