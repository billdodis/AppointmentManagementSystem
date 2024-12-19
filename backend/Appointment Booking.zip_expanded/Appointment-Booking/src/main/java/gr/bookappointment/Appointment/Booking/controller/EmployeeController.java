package gr.bookappointment.Appointment.Booking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gr.bookappointment.Appointment.Booking.dto.input.EmployeeInputDTO;
import gr.bookappointment.Appointment.Booking.dto.input.LoginDTO;
import gr.bookappointment.Appointment.Booking.dto.output.AuthResponseDTO;
import gr.bookappointment.Appointment.Booking.dto.output.EmployeeDTO;
import gr.bookappointment.Appointment.Booking.dto.output.RoleEnum;
import gr.bookappointment.Appointment.Booking.security.JwtTokenProvider;
import gr.bookappointment.Appointment.Booking.service.EmployeeService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/employees")
public class EmployeeController implements Controller{
	
	private final EmployeeService emplService;
	private final JwtTokenProvider jwtProvider;
	
	public EmployeeController(EmployeeService emplService, 	JwtTokenProvider jwtProvider) {
		this.emplService = emplService;
		this.jwtProvider = jwtProvider;
	}
	
	@PostMapping("/addEmployee")
	public ResponseEntity<String> addEmployee(@RequestBody EmployeeInputDTO employeeDTO) {
		return emplService.addEmployee(employeeDTO);
	}
	
	@GetMapping("/getEmployee")
	public EmployeeDTO getEmployeeById(long id) {
		return emplService.getEmployeeById(id);
	}
	
	@GetMapping("/getEmployees")
	public List<EmployeeDTO> getEmployees(){
		return emplService.getEmployees();
	}
	
	@PostMapping("/loginEmployee")
	@CrossOrigin("*")
	public ResponseEntity<?> loginEmployee(@RequestBody LoginDTO loginDTO) {
		boolean isUserValid = emplService.loginEmployee(loginDTO);
		if (isUserValid) {
			AuthResponseDTO authResponse = new AuthResponseDTO(jwtProvider.generateToken(loginDTO.getEmail()),
							emplService.getEmployeeName(loginDTO), RoleEnum.EMPLOYEE,
							emplService.getEmployeeId(loginDTO));
            return ResponseEntity.ok(authResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	}
	
	@PutMapping("/editEmployee")
	public void editEmployee(@RequestBody EmployeeInputDTO employeeDTO) {
		emplService.editEmployee(employeeDTO);
	}
	
	@DeleteMapping("/deleteEmployeeById")
	public void deleteEmployeeById(long id) {
		emplService.deleteEmployeeById(id);
	}
	
}
