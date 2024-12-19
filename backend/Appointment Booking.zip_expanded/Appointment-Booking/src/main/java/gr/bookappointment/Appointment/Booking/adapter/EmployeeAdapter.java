package gr.bookappointment.Appointment.Booking.adapter;

import org.springframework.stereotype.Component;

import gr.bookappointment.Appointment.Booking.dto.input.EmployeeInputDTO;
import gr.bookappointment.Appointment.Booking.dto.output.EmployeeDTO;
import gr.bookappointment.Appointment.Booking.model.Employee;
import gr.bookappointment.Appointment.Booking.security.PasswordHasher;

@Component
public class EmployeeAdapter implements Adapter<EmployeeInputDTO, Employee, EmployeeDTO>{

	@Override
	public Employee toEntity(EmployeeInputDTO dto) {
		Employee employee = new Employee.EmployeeBuilder()
					.setFirstName(dto.getFirstName())
					.setLastName(dto.getLastName())
					.setEmail(dto.getEmail())
					.setPassword(PasswordHasher.hashPassword(dto.getPassword()))
					.build();
		return employee;
	}

	@Override
	public EmployeeDTO toDTO(Employee employee) {
		return new EmployeeDTO(employee.getId(), employee.getFirstName(),
				employee.getLastName(), employee.getEmail());
	}

}
