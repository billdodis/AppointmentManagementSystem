package gr.bookappointment.Appointment.Booking.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gr.bookappointment.Appointment.Booking.dto.input.ChangeUserDataDTO;
import gr.bookappointment.Appointment.Booking.dto.input.LoginDTO;
import gr.bookappointment.Appointment.Booking.dto.input.UserInputDTO;
import gr.bookappointment.Appointment.Booking.dto.output.AuthResponseDTO;
import gr.bookappointment.Appointment.Booking.dto.output.RoleEnum;
import gr.bookappointment.Appointment.Booking.dto.output.UserDTO;
import gr.bookappointment.Appointment.Booking.security.JwtTokenProvider;
import gr.bookappointment.Appointment.Booking.service.UserService;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/users")
public class UserController implements Controller{
	
	private final UserService userService;
	private final JwtTokenProvider jwtProvider;
	
	public UserController(UserService userService, JwtTokenProvider jwtProvider) {
		this.userService = userService;
		this.jwtProvider = jwtProvider;
	}

	@PostMapping("/addUser")
	public ResponseEntity<String> addUser(@RequestBody UserInputDTO userInpDTO) {
		return userService.addUser(userInpDTO);
	}
	
	@GetMapping("/getUser")
	public UserDTO getUserById(long id) {
		return userService.getUserById(id);
	}
	
	@GetMapping("/getUsers")
	public List<UserDTO> getUsers(){
		return userService.getUsers();
	}
	
	@PostMapping("/loginUser")
	@CrossOrigin("*")
	public ResponseEntity<?> loginUser(@RequestBody LoginDTO loginDTO) {
		boolean isUserValid = userService.loginUser(loginDTO);
		if (isUserValid) {
			AuthResponseDTO authResponse = new AuthResponseDTO(jwtProvider.generateToken(loginDTO.getEmail()),
							userService.getUserName(loginDTO), RoleEnum.USER,
							userService.getUserId(loginDTO));
            return ResponseEntity.ok(authResponse);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
	}
	
	@PutMapping("/editUser/{userId}")
	public ResponseEntity<String> editUser(@PathVariable long userId, @RequestBody ChangeUserDataDTO changeUserDataDTO) {
		return userService.editUser(userId, changeUserDataDTO);
	}
	
	@DeleteMapping("/deleteUserById")
	public void deleteUserById(long id) {
		userService.deleteUserById(id);
	}
	
	@DeleteMapping("/deleteUsers")
	public void deleteUsers() {
		userService.deleteUsers();
	}
	
}
