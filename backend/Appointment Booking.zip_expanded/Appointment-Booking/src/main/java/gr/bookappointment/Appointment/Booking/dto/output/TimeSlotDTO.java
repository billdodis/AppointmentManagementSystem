package gr.bookappointment.Appointment.Booking.dto.output;

import java.time.LocalTime;
import java.util.List;

public class TimeSlotDTO {
	private LocalTime time;
	private List<EmployeeDTO> employeeList;
	
	public TimeSlotDTO() {}
	
	public TimeSlotDTO(LocalTime time, List<EmployeeDTO> employeeList) {
		this.time = time;
		this.employeeList = employeeList;
	}
	
	public LocalTime getTime(){
		return time;
	}
	
	public List<EmployeeDTO> getEmployeeList(){
		return employeeList;
	}
	
	public void setTime(LocalTime time) {
		this.time = time;
	}
	
	public void setEmployeeList(List<EmployeeDTO> employeeList) {
		this.employeeList = employeeList;
	}
}
