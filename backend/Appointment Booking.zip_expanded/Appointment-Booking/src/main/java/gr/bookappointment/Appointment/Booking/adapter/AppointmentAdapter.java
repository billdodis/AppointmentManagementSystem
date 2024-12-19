package gr.bookappointment.Appointment.Booking.adapter;

import org.springframework.stereotype.Component;

import gr.bookappointment.Appointment.Booking.dto.input.AppointmentInputDTO;
import gr.bookappointment.Appointment.Booking.dto.output.AppointmentDTO;
import gr.bookappointment.Appointment.Booking.model.Appointment;

@Component
public class AppointmentAdapter implements Adapter<AppointmentInputDTO, Appointment, AppointmentDTO>{

	@Override
	public Appointment toEntity(AppointmentInputDTO dto) {
		Appointment appointment = new Appointment.AppointmentBuilder()
							.setUserId(dto.getUserId())
							.setEmployeeId(dto.getEmId())
							.setDate(dto.getDate())
							.build();
		return appointment;
	}

	@Override
	public AppointmentDTO toDTO(Appointment appointment) {
		return null;
	}

}
