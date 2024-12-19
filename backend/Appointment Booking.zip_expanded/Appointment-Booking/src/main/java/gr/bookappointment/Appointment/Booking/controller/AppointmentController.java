package gr.bookappointment.Appointment.Booking.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gr.bookappointment.Appointment.Booking.dto.input.AppointmentInputDTO;
import gr.bookappointment.Appointment.Booking.dto.output.AppointmentDTO;
import gr.bookappointment.Appointment.Booking.dto.output.TimeSlotDTO;
import gr.bookappointment.Appointment.Booking.service.AppointmentService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/appointments")
public class AppointmentController implements Controller{

	private final AppointmentService apService;
	
	public AppointmentController(AppointmentService apService) {
		this.apService = apService;
	}
	
	@PostMapping("/addAppointment")
	public ResponseEntity<String> addAppointment(@RequestBody AppointmentInputDTO appointmentInpDTO) {
		return apService.addAppointment(appointmentInpDTO);
	}
	
	@GetMapping("/getTimeslots")
	public List<TimeSlotDTO> getTimeslots(@RequestParam("date") LocalDateTime date){
		return apService.getTimeslots(date);
	}
	
	@GetMapping("/getAppointment")
	public AppointmentDTO getAppointmentById(long id) {
		return apService.getAppointmentById(id);
	}
	
	@GetMapping("/getAppointments")
	public List<AppointmentDTO> getAppointments(){
		return apService.getAppointments();
	}
	
	@GetMapping("getAppointmentsByEmId/{emid}")
	public List<AppointmentDTO> getAppointmentsByEmId(@PathVariable long emid){
		return apService.getAppointmentsByEmId(emid);
	}
	
	@GetMapping("getAppointmentsByUserId/{userid}")
	public List<AppointmentDTO> getAppointmentsByUserId(@PathVariable long userid){
		return apService.getAppointmentsByUserId(userid);
	}
	
	@PutMapping("/editAppointment")
	public void editAppointment(@RequestBody AppointmentInputDTO appointmentInpDTO) {
		apService.editAppointment(appointmentInpDTO);
	}
	
	@DeleteMapping("/deleteAppointmentById/{id}")
	public void deleteAppointmentById(@PathVariable long id) {
		apService.deleteAppointmentById(id);
	}
	
	@DeleteMapping("/deleteAppointments")
	public void deleteAppointments() {
		apService.deleteAppointments();
	}
	
}
