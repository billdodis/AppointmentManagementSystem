package gr.bookappointment.Appointment.Booking.service;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import gr.bookappointment.Appointment.Booking.adapter.Adapter;
import gr.bookappointment.Appointment.Booking.dto.input.ChangeUserDataDTO;
import gr.bookappointment.Appointment.Booking.dto.input.LoginDTO;
import gr.bookappointment.Appointment.Booking.dto.input.UserInputDTO;
import gr.bookappointment.Appointment.Booking.dto.output.UserDTO;
import gr.bookappointment.Appointment.Booking.model.User;
import gr.bookappointment.Appointment.Booking.repository.UserRepository;
import gr.bookappointment.Appointment.Booking.security.PasswordHasher;

@Service
public class UserService{
	
	private final UserRepository userRepo;
	private final Adapter<UserInputDTO, User, UserDTO> userAdapter;
	
	public UserService(UserRepository userRepo, Adapter<UserInputDTO, User, UserDTO> userAdapter) {
		this.userRepo = userRepo;
		this.userAdapter = userAdapter;
	}

	public ResponseEntity<String> addUser(UserInputDTO userInpDTO) {
		Optional<User> optionalUser = userRepo.findByEmail(userInpDTO.getEmail());
		if(!optionalUser.isPresent()) {
			User user = userAdapter.toEntity(userInpDTO);
			userRepo.save(user);
		    return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully.");
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists!");
		}
	}
	
	public UserDTO getUserById(long id) {
		User user = userRepo.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));
		return userAdapter.toDTO(user);
	}

	public ArrayList<UserDTO> getUsers() {
		ArrayList<UserDTO> userList = new ArrayList<UserDTO>();
		for(User user : userRepo.findAll()) {
			userList.add(userAdapter.toDTO(user));
		}
		return userList;
	}

	public ResponseEntity<String> editUser(long userId, ChangeUserDataDTO changeUserDataDTO) {
		User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found!"));
		if(PasswordHasher.isPasswordCorrect(changeUserDataDTO.getOldPassword(), user.getPassword())) {
			user.setFirstName(changeUserDataDTO.getFirstName());
			user.setLastName(changeUserDataDTO.getLastName());
			user.setEmail(changeUserDataDTO.getEmail());
			if(changeUserDataDTO.getPassword() != "") {
				user.setPassword(PasswordHasher.hashPassword(changeUserDataDTO.getPassword()));
			}
			userRepo.save(user);
			return ResponseEntity.status(HttpStatus.CREATED).body("User updated successfully.");
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Wrong old password!");
		}
	}

	public void deleteUserById(long id) {
		userRepo.deleteById(id);
	}

	public void deleteUser(User user) {
		userRepo.delete(user);		
	}

	public boolean loginUser(LoginDTO loginDTO) {
		Optional<User> optionalUser = userRepo.findByEmail(loginDTO.getEmail());
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			if (PasswordHasher.isPasswordCorrect(loginDTO.getPassword(), user.getPassword())) {
				return true;
			}
		}
		return false;
	}

	public String getUserName(LoginDTO loginDTO) {
		Optional<User> optionalUser = userRepo.findByEmail(loginDTO.getEmail());
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			return user.getFirstName() + " " + user.getLastName();
		}
		else {
			return "";
		}
	}
	
	public long getUserId(LoginDTO loginDTO) {
		Optional<User> optionalUser = userRepo.findByEmail(loginDTO.getEmail());
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			return user.getId();
		}
		else {
			return 0;
		}
	}

	public void deleteUsers() {
		userRepo.deleteAll();
	}
}
