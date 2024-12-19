package gr.bookappointment.Appointment.Booking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import gr.bookappointment.Appointment.Booking.adapter.Adapter;
import gr.bookappointment.Appointment.Booking.dto.input.AppointmentInputDTO;
import gr.bookappointment.Appointment.Booking.dto.output.AppointmentDTO;
import gr.bookappointment.Appointment.Booking.dto.output.EmployeeDTO;
import gr.bookappointment.Appointment.Booking.dto.output.TimeSlotDTO;
import gr.bookappointment.Appointment.Booking.model.Appointment;
import gr.bookappointment.Appointment.Booking.repository.AppointmentRepository;

@Service
public class AppointmentService{

	private final AppointmentRepository apRepo;
	private final Adapter<AppointmentInputDTO, Appointment, AppointmentDTO> appointmentAdapter;
	private final EmployeeService employeeService;
	private final UserService userService;
	
	
	public AppointmentService(AppointmentRepository apRepo, Adapter<AppointmentInputDTO, Appointment, AppointmentDTO> appointmentAdapter, EmployeeService employeeService, UserService userService) {
		this.apRepo = apRepo;
		this.appointmentAdapter = appointmentAdapter;
		this.employeeService = employeeService;
		this.userService = userService;
	}

	public ResponseEntity<String> addAppointment(AppointmentInputDTO appointmentInpDTO) {
		Appointment appointment = appointmentAdapter.toEntity(appointmentInpDTO);
		apRepo.save(appointment);
	    return ResponseEntity.status(HttpStatus.CREATED).body("Appointment created successfully.");
	}

	public AppointmentDTO getAppointmentById(long id) {
		Appointment appointment = apRepo.findById(id).orElseThrow(() -> new RuntimeException("Appointment not found!"));
		return new AppointmentDTO(appointment.getId(), userService.getUserById(appointment.getUserId()),
				employeeService.getEmployeeById(appointment.getEmployeeId()), appointment.getDate());
	}

	public ArrayList<AppointmentDTO> getAppointments() {
		ArrayList<AppointmentDTO> apList = new ArrayList<AppointmentDTO>();
		for(Appointment appointment : apRepo.findAll()) {
			apList.add(new AppointmentDTO(appointment.getId(), userService.getUserById(appointment.getUserId()), 
					employeeService.getEmployeeById(appointment.getEmployeeId()), appointment.getDate()));
		}
		return apList;
	}

	public void editAppointment(AppointmentInputDTO appointmentDTO) {
		Appointment appointment = apRepo.findById(appointmentDTO.getApId()).orElseThrow(() -> new RuntimeException("Appointment not found!"));
		appointment.setUserId(appointmentDTO.getUserId());
		appointment.setEmployeeId(appointmentDTO.getEmId());
		appointment.setDate(appointmentDTO.getDate());
		apRepo.save(appointment);
	}

	public void deleteAppointmentById(long id) {
		apRepo.deleteById(id);		
	}

	public void deleteAppointment(Appointment appointment) {
		apRepo.delete(appointment);
	}

	public List<TimeSlotDTO> getTimeslots(LocalDateTime dateTime) {
		ArrayList<TimeSlotDTO> timeSlotList = new ArrayList<TimeSlotDTO>();
		ArrayList<LocalTime> workingHours = new ArrayList<LocalTime>();
		LocalTime[] weekdayHours = {LocalTime.of(9, 0), LocalTime.of(14, 00), LocalTime.of(17, 30), LocalTime.of(21, 00)};
        LocalTime[] saturdayHours = {LocalTime.of(9, 0), LocalTime.of(14, 00)};
        initializeWorkingHours(dateTime, weekdayHours, saturdayHours, workingHours);
		for (int i = 0; i < workingHours.size(); i += 2) {
            LocalTime startTime = workingHours.get(i);
            LocalTime endTime = workingHours.get(i+1);
            generateTimeSlots(dateTime.toLocalDate(), startTime, endTime, timeSlotList);
        }
		return timeSlotList;
	}

	public List<AppointmentDTO> getAppointmentsByEmId(long emId) {
		ArrayList<AppointmentDTO> apListDTO = new ArrayList<AppointmentDTO>();
		for(Appointment appointment: apRepo.findByEmid(emId)) {
			apListDTO.add(new AppointmentDTO(appointment.getId(), userService.getUserById(appointment.getUserId()), 
					employeeService.getEmployeeById(appointment.getEmployeeId()), appointment.getDate()));
		}
		return apListDTO;
	}

	public List<AppointmentDTO> getAppointmentsByUserId(long userId) {
		ArrayList<AppointmentDTO> apListDTO = new ArrayList<AppointmentDTO>();
		for(Appointment appointment: apRepo.findByUserid(userId)) {
			apListDTO.add(new AppointmentDTO(appointment.getId(), userService.getUserById(appointment.getUserId()),
					employeeService.getEmployeeById(appointment.getEmployeeId()), appointment.getDate()));
		}
		return apListDTO;
	}
	
	private void initializeWorkingHours(LocalDateTime dateTime, LocalTime[] weekdayHours, LocalTime[] saturdayHours, ArrayList<LocalTime> workingHours) {
		if (dateTime.getDayOfWeek().toString().equals("SATURDAY")) {
			for(LocalTime hour: saturdayHours) {
				workingHours.add(hour);
			}
		}else {
			for(LocalTime hour: weekdayHours) {
				workingHours.add(hour);
			}
		}
	}
	
	private void generateTimeSlots(LocalDate date, LocalTime startTime, LocalTime endTime, ArrayList<TimeSlotDTO> timeSlotList) {
		LocalTime currentTime = startTime;
        while (currentTime.isBefore(endTime)) {
            LocalDateTime currentDateTime = LocalDateTime.of(date, currentTime);
            List<Appointment> apList = getAppointmentsByDate(currentDateTime);
            List<EmployeeDTO> employeeList = getEmployeesFromAppointment(apList);
            if(employeeList.size() > 0) {
            	timeSlotList.add(new TimeSlotDTO(currentTime, employeeList));
            }
            currentTime = currentTime.plusMinutes(30);
        }
	}

	private List<Appointment> getAppointmentsByDate(LocalDateTime date){
		return apRepo.findByDate(date);
	}
	
	private List<EmployeeDTO> getEmployeesFromAppointment(List<Appointment> apList){
		if (apList == null) {
			return null;
		}else {
		List<EmployeeDTO> emList = employeeService.getEmployees();
		for(Appointment appointment: apList) {
			emList.removeIf(employeeDTO -> employeeDTO.getEmId() == (appointment.getEmployeeId()));
		}
			return emList;
		}
	}

	public void deleteAppointments() {
		apRepo.deleteAll();
	}
	
}
