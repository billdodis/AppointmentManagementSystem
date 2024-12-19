package gr.bookappointment.Appointment.Booking.adapter;

import org.springframework.stereotype.Component;

import gr.bookappointment.Appointment.Booking.dto.input.UserInputDTO;
import gr.bookappointment.Appointment.Booking.dto.output.UserDTO;
import gr.bookappointment.Appointment.Booking.model.User;
import gr.bookappointment.Appointment.Booking.security.PasswordHasher;

@Component
public class UserAdapter implements Adapter<UserInputDTO, User, UserDTO>{

	@Override
	public User toEntity(UserInputDTO dto) {
		User user = new User.UserBuilder()
				.setFirstName(dto.getFirstName())
				.setLastName(dto.getLastName())
				.setEmail(dto.getEmail())
				.setPassword(PasswordHasher.hashPassword(dto.getPassword()))
				.build();
		return user;
	}

	@Override
	public UserDTO toDTO(User user) {
		return new UserDTO(user.getId(), user.getFirstName(),
				user.getLastName(), user.getEmail());	
	}

}
