package gr.bookappointment.Appointment.Booking.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtTokenProvider {
	
	@Value("${jwt.secret}")
	private String secretKey;
	private Key key;
	private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 10; // 10h
	
	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
		this.secretKey = secretKey;
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
	}

	public String generateToken(String email) {
		return Jwts.builder().setSubject(email).setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	public String extractEmail(String token) {
		JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
		return parser.parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateToken(String token, String email) {
		String extractedEmail = extractEmail(token);
		return extractedEmail.equals(email) && !isTokenExpired(token);
	}

	private boolean isTokenExpired(String token) {
		JwtParser parser = Jwts.parserBuilder().setSigningKey(key).build();
		return parser.parseClaimsJws(token).getBody().getExpiration().before(new Date());
	}
}
