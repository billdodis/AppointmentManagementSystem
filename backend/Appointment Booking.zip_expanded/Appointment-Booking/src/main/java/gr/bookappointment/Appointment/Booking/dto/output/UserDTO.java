package gr.bookappointment.Appointment.Booking.dto.output;

import lombok.Setter;

@Setter
public class UserDTO {
	
	private long userid;
	private String firstname;
	private String lastname;
	private String email;
	
	public UserDTO() {}
	
	public UserDTO(long user_id, String first_name, String last_name, String email) {
		this.userid = user_id;
		this.firstname = first_name;
		this.lastname = last_name;
		this.email = email;
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

}
