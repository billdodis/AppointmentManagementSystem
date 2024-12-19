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
@Table(name = "employee")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long emid;
	@Column(name = "firstname")
	private String firstname;
	@Column(name = "lastname")
	private String lastname;
	@Column(name = "email", unique = true)
	private String email;
	@Column(name = "password")
	private String password;
	
	public Employee() {}
	
	private Employee(EmployeeBuilder builder) {
        this.emid = builder.emid;
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
	
	public static class EmployeeBuilder {
        private long emid;
        private String firstname;
        private String lastname;
        private String email;
        private String password;

        public EmployeeBuilder setEmployeeid(long emid) {
            this.emid = emid;
            return this;
        }

        public EmployeeBuilder setFirstName(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public EmployeeBuilder setLastName(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public EmployeeBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public EmployeeBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Employee build() {
            return new Employee(this);
        }
	}
}
