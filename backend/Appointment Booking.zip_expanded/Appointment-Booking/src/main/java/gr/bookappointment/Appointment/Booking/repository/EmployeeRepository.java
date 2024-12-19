package gr.bookappointment.Appointment.Booking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gr.bookappointment.Appointment.Booking.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	Optional<Employee> findByEmail(String email);
}
