package gr.bookappointment.Appointment.Booking.dto.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Setter;

@Setter
public class EmployeeInputDTO {
	
	@JsonProperty("emId")
	private long emid;
	@JsonProperty("firstName")
	private String firstname;
	@JsonProperty("lastName")
	private String lastname;
	private String email;
	private String password;
	
	public EmployeeInputDTO() {}
	
	public EmployeeInputDTO(long em_id, String first_name, String last_name, String email, String password) {
		this.emid = em_id;
		this.firstname = first_name;
		this.lastname = last_name;
		this.email = email;
		this.password = password;
	}
	
	public EmployeeInputDTO(String firstName, String lastName, String email, String password) {
		this.firstname = firstName;
		this.lastname = lastName;
		this.email = email;
		this.password = password;
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
	
	public String getPassword() {
		return password;
	}
	
	public void setEmId(long em_id) {
		this.emid = em_id;
	}
	
}
