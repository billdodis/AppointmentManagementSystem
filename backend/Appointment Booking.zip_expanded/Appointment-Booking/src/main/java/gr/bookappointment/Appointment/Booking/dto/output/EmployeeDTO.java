package gr.bookappointment.Appointment.Booking.dto.output;

import lombok.Setter;

@Setter
public class EmployeeDTO {
	
	private long emid;
	private String firstname;
	private String lastname;
	private String email;
	
	public EmployeeDTO() {}
	
	public EmployeeDTO(long em_id, String first_name, String last_name, String email) {
		this.emid = em_id;
		this.firstname = first_name;
		this.lastname = last_name;
		this.email = email;
	}
	
	public EmployeeDTO(String firstName, String lastName, String email) {
		this.firstname = firstName;
		this.lastname = lastName;
		this.email = email;
	}
	
	public long getEmId() {
		return emid;
	}
	
	public String getFirstName() {
		return firstname;
	}
	
	public String getLastName() {
		return lastname;
	}
	
	public String getEmail() {
		return email;
	}
	
}
