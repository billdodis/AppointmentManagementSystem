package gr.bookappointment.Appointment.Booking.adapter;

public interface Adapter<ID, E, D> {
	
	E toEntity(ID inputDto);

	D toDTO(E entity);
}
