package gr.bookappointment.Appointment.Booking.dto.input;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Setter;

@Setter
public class LoginDTO {

	@JsonProperty("email")
	private String email;
	@JsonProperty("password")
	private String password;

	public LoginDTO() {
	}

	public LoginDTO(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
