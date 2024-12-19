package gr.bookappointment.Appointment.Booking.controller;

public enum ControllerType {
	USER,
	APPOINTMENT,
	EMPLOYEE;

	public static ControllerType getTypeFromString(String controllerType) {
		return ControllerType.valueOf(controllerType);
	}
}
