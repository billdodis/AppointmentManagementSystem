package gr.bookappointment.Appointment.Booking.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import gr.bookappointment.Appointment.Booking.adapter.Adapter;
import gr.bookappointment.Appointment.Booking.dto.input.EmployeeInputDTO;
import gr.bookappointment.Appointment.Booking.dto.input.LoginDTO;
import gr.bookappointment.Appointment.Booking.dto.output.EmployeeDTO;
import gr.bookappointment.Appointment.Booking.model.Employee;
import gr.bookappointment.Appointment.Booking.repository.EmployeeRepository;
import gr.bookappointment.Appointment.Booking.security.PasswordHasher;

@Service
public class EmployeeService{
	
	private final EmployeeRepository emplRepo;
	private final Adapter<EmployeeInputDTO, Employee, EmployeeDTO> employeeAdapter;
	
	public EmployeeService(EmployeeRepository emplRepo, Adapter<EmployeeInputDTO, Employee, EmployeeDTO> employeeAdapter) {
		this.emplRepo = emplRepo;
		this.employeeAdapter = employeeAdapter;
	}

	public ResponseEntity<String> addEmployee(EmployeeInputDTO employeeInpDTO) {
		Optional<Employee> optionalEmployee = emplRepo.findByEmail(employeeInpDTO.getEmail());
		if(!optionalEmployee.isPresent()) {
			Employee employee = employeeAdapter.toEntity(employeeInpDTO);
			emplRepo.save(employee);
		    return ResponseEntity.status(HttpStatus.CREATED).body("Employee created successfully.");
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists!");
		}
	}
	
	public EmployeeDTO getEmployeeById(long id) {
		Employee employee = emplRepo.findById(id).orElseThrow(() -> new RuntimeException("Employee not found!"));
		return employeeAdapter.toDTO(employee);
	}

	public ArrayList<EmployeeDTO> getEmployees() {
		ArrayList<EmployeeDTO> emplList = new ArrayList<EmployeeDTO>();
		for(Employee employee : emplRepo.findAll()) {
			emplList.add(employeeAdapter.toDTO(employee));
		}
		return emplList;
	}
	
	public void editEmployee(EmployeeInputDTO employeeInpDTO) {
		Employee employee = emplRepo.findById(employeeInpDTO.getEmId()).orElseThrow(() -> new RuntimeException("Employee not found!"));
		employee.setFirstName(employeeInpDTO.getFirstName());
		employee.setLastName(employeeInpDTO.getLastName());
		employee.setEmail(employeeInpDTO.getEmail());
		employee.setPassword(PasswordHasher.hashPassword(employeeInpDTO.getPassword()));
		emplRepo.save(employee);
	}

	public void deleteEmployeeById(long id) {
		emplRepo.deleteById(id);
	}

	public void deleteEmployee(Employee employee) {
		emplRepo.delete(employee);
	}

	public boolean loginEmployee(LoginDTO loginDTO) {
		Optional<Employee> optionalEmployee = emplRepo.findByEmail(loginDTO.getEmail());
		if (optionalEmployee.isPresent()) {
			Employee employee = optionalEmployee.get();
			if (PasswordHasher.isPasswordCorrect(loginDTO.getPassword(), employee.getPassword())) {
				return true;
			}
		}
		return false;
	}

	public String getEmployeeName(LoginDTO loginDTO) {
		Optional<Employee> optionalEmployee = emplRepo.findByEmail(loginDTO.getEmail());
		if (optionalEmployee.isPresent()) {
			Employee employee = optionalEmployee.get();
			return employee.getFirstName() + " " + employee.getLastName();
		}
		else {
			return "";
		}
	}
	
	public long getEmployeeId(LoginDTO loginDTO) {
		Optional<Employee> optionalEmployee = emplRepo.findByEmail(loginDTO.getEmail());
		if (optionalEmployee.isPresent()) {
			Employee employee = optionalEmployee.get();
			return employee.getId();
		}
		else {
			return 0;
		}
	}
}
