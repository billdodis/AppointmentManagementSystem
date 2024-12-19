package gr.bookappointment.Appointment.Booking.dto.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Setter;

@Setter
public class UserInputDTO {
	
	@JsonProperty("userId")
	private long userid;
	private String email;
	private String password;
	@JsonProperty("firstName")
	private String firstname;
	@JsonProperty("lastName")
	private String lastname;
	
	
	public UserInputDTO() {}
	
	public UserInputDTO(long user_id, String first_name, String last_name, String email, String password) {
		this.userid = user_id;
		this.firstname = first_name;
		this.lastname = last_name;
		this.email = email;
		this.password = password;
	}
	
	public UserInputDTO(String first_name, String last_name, String email, String password){
		this.firstname = first_name;
		this.lastname = last_name;
		this.email = email;
		this.password = password;
	}
	
	public long getId() {
		return userid;
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
	
	public void setId(long user_id) {
		this.userid = user_id;
	}

}
