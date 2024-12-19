package gr.bookappointment.Appointment.Booking.dto.output;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AppointmentDTO {

	private long apid;
	private UserDTO user;
	private EmployeeDTO employee;
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime date;
	
	public AppointmentDTO() {}
	
	public AppointmentDTO(long ap_id, UserDTO user, EmployeeDTO employee, LocalDateTime ap_date) {
		this.apid = ap_id;
		this.user = user;
		this.employee = employee;
		this.date = ap_date;
	}
	
	public AppointmentDTO(UserDTO user, EmployeeDTO employee, LocalDateTime ap_date) {
		this.user = user;
		this.employee = employee;
		this.date = ap_date;
	}
	
	public long getApId() {
		return apid;
	}
	
	public UserDTO getUser() {
		return user;
	}
	
	public EmployeeDTO getEmployee() {
		return employee;
	}
	
	public LocalDateTime getDate() {
		return date;
	}
	
	public void setApId(long ap_id) {
		this.apid = ap_id;
	}

}
