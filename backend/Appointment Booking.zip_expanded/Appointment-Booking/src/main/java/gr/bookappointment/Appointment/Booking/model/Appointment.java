package gr.bookappointment.Appointment.Booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@Entity
@Table(name = "appointment")
public class Appointment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long apid;
	@Column(name = "userid")
	private long userid;
	@Column(name = "emid")
	private long emid;
	@Column(name = "date")
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	private LocalDateTime date;
	
	public Appointment() {}
	
	private Appointment(AppointmentBuilder builder) {
		this.apid = builder.apid;
        this.userid = builder.userid;
        this.emid = builder.emid;
        this.date = builder.date;
	}
	
	public void setUserId(long user_id) {
		this.userid = user_id;
	}
	
	public void setEmployeeId(long em_id) {
		this.emid = em_id;
	}
	
	public void setDate(LocalDateTime ap_date) {
		this.date = ap_date;
	}
	
	public long getId() {
		return apid;
	}
	
	public long getUserId() {
		return userid;
	}
	
	public long getEmployeeId() {
		return emid;
	}
	
	public LocalDateTime getDate() {
		return date;
	}
	
	public static class AppointmentBuilder {
        private long apid;
        private long userid;
        private long emid;
        private LocalDateTime date;

        public AppointmentBuilder setId(long apid) {
            this.apid = apid;
            return this;
        }

        public AppointmentBuilder setUserId(long userid) {
            this.userid = userid;
            return this;
        }

        public AppointmentBuilder setEmployeeId(long emid) {
            this.emid = emid;
            return this;
        }

        public AppointmentBuilder setDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Appointment build() {
            return new Appointment(this);
        }
	}

}
