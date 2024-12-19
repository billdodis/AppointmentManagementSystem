package gr.bookappointment.Appointment.Booking.security;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHasher {
	
	public static String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt(12);
        return BCrypt.hashpw(plainPassword, salt);
    }
	
	public static boolean isPasswordCorrect(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
