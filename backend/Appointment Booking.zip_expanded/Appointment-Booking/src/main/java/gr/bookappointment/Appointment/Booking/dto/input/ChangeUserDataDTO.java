package gr.bookappointment.Appointment.Booking.dto.input;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangeUserDataDTO {
	 
	@JsonProperty("firstName")
	private String firstname;
	@JsonProperty("lastName")
	private String lastname;
	private String email;
	private String password;
	@JsonProperty("oldPassword")
	private String oldPassword;
	
	public ChangeUserDataDTO() {}
	
	public ChangeUserDataDTO(String firstName, String lastName, String email, String oldPassword) {
		this.firstname = firstName;
		this.lastname = lastName;
		this.email = email;
		this.oldPassword = oldPassword;
	}
	
	public ChangeUserDataDTO(String firstName, String lastName, String email, String oldPassword, String password) {
		this.firstname = firstName;
		this.lastname = lastName;
		this.email = email;
		this.oldPassword = oldPassword;
		this.password = password;
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
	
	public String getOldPassword() {
		return oldPassword;
	}
	
	public String getPassword() {
		return password;
	}

}
