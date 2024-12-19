package gr.bookappointment.Appointment.Booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

// (exclude = {DataSourceAutoConfiguration.class })

public class AppointmentBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppointmentBookingApplication.class, args);
		new WelcomeMessage().printMessage();
	}
}
