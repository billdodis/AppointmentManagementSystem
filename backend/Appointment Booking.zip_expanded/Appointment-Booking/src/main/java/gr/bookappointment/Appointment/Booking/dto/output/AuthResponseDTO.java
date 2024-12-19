package gr.bookappointment.Appointment.Booking.dto.output;

public class AuthResponseDTO {
	
	private String token;
	private String name;
	private RoleEnum role;
	private long id; // User ID or Employee ID!
	
	public AuthResponseDTO() {
	}
	
	public AuthResponseDTO(String token, String name, RoleEnum role, long id) {
		this.token = token;
		this.name = name;
		this.role = role;
		this.id = id;
		
	}
	
	public String getToken() {
		return token;
	}
	
	public String getName() {
		return name;
	}
	
	public RoleEnum getRole() {
		return role;
	}
	
	public long getId() {
		return id;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setRole(RoleEnum role) {
		this.role = role;
	}
	
	public void setId(long id) {
		this.id = id;
	}

}
