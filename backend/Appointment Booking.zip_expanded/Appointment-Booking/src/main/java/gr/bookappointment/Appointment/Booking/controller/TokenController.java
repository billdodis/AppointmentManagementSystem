package gr.bookappointment.Appointment.Booking.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gr.bookappointment.Appointment.Booking.security.JwtTokenProvider;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/token")
public class TokenController {
	
	private final JwtTokenProvider jwtProvider;
	
	public TokenController (JwtTokenProvider jwtProvider) {
		this.jwtProvider = jwtProvider;
		
	}
	
	@PostMapping("/validateToken")
	public ResponseEntity<String> loginUser(@RequestHeader("Authorization") String authorizedToken) {
		try {
			String token = authorizedToken.replace("Bearer ", "");
			String email = jwtProvider.extractEmail(token);
			if(jwtProvider.validateToken(token, email)) {
	            return ResponseEntity.ok("Token is valid!");
			}else {
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
			}
		} catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while validating the token");
	    }
	}
}
