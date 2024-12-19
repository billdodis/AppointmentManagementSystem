package gr.bookappointment.Appointment.Booking.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userid;
	@Column(name = "firstname")
	private String firstname;
	@Column(name = "lastname")
	private String lastname;
	@Column(name = "email", unique = true)
	private String email;
	@Column(name = "password")
	private String password;
	
	public User() {}
	
	private User(UserBuilder builder) {
        this.userid = builder.userid;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.email = builder.email;
        this.password = builder.password;
    }
	
	public void setFirstName(String firstName) {
		this.firstname = firstName;
	}
	
	public void setLastName(String lastName) {
		this.lastname = lastName;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
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
	
	public static class UserBuilder {
        private long userid;
        private String firstname;
        private String lastname;
        private String email;
        private String password;

        public UserBuilder setUserid(long userid) {
            this.userid = userid;
            return this;
        }

        public UserBuilder setFirstName(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public UserBuilder setLastName(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}
