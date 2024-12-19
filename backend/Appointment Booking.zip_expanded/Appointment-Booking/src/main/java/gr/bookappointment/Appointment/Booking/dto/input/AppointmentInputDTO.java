package gr.bookappointment.Appointment.Booking.dto.input;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentInputDTO {
	
	@JsonProperty("apId")
	private long apid;
	@JsonProperty("userId")
	private long userid;
	@JsonProperty("emId")
	private long emid;
	@JsonProperty("date")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime date;
	
	public AppointmentInputDTO() {}
	
	public AppointmentInputDTO(long apid, long userid, long emid, LocalDateTime date) {
		this.apid = apid;
		this.userid = userid;
		this.emid = emid;
		this.date = date;
	}
	
	public AppointmentInputDTO(long userid, long emid, LocalDateTime date) {
		this.userid = userid;
		this.emid = emid;
		this.date = date;
	}
	
	public long getApId() {
		return apid;
	}
	
	public long getUserId() {
		return userid;
	}
	
	public long getEmId() {
		return emid;
	}
	
	public LocalDateTime getDate() {
		return date;
	}
	
	public void setApId(long apid) {
		this.apid = apid;
	}

}
