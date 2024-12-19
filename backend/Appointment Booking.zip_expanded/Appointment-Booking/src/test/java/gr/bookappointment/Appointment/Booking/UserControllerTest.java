package gr.bookappointment.Appointment.Booking;

import com.fasterxml.jackson.databind.ObjectMapper;

import gr.bookappointment.Appointment.Booking.dto.input.ChangeUserDataDTO;
import gr.bookappointment.Appointment.Booking.dto.input.LoginDTO;
import gr.bookappointment.Appointment.Booking.dto.input.UserInputDTO;
import gr.bookappointment.Appointment.Booking.model.User;
import gr.bookappointment.Appointment.Booking.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserRepository userRepository;

    private ObjectMapper objectMapper;
  
    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    public void testAddUser() throws Exception {
    	UserInputDTO userInputDTO = new UserInputDTO("John", "Doe", "johndoe@example.com", "pass");
        mockMvc.perform(post("/api/users/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInputDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("User created successfully."));
    }
    
    @Test
    @Order(2)
    public void testExistingEmailAddUser() throws Exception {
    	UserInputDTO userInputDTO = new UserInputDTO("John", "Doe", "johndoe@example.com", "pass");
        mockMvc.perform(post("/api/users/addUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userInputDTO)))
        		.andExpect(MockMvcResultMatchers.status().isBadRequest())
        		.andExpect(MockMvcResultMatchers.content().string("Email already exists!"));
    }

    @Test
    @Order(3)
    public void testLoginUser() throws Exception {
        LoginDTO loginDTO = new LoginDTO("johndoe@example.com", "pass");
        mockMvc.perform(post("/api/users/loginUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("USER"));
    }
    
    @Test
    @Order(4)
    public void testLoginUserWrongCredentials() throws Exception {
        LoginDTO loginDTO = new LoginDTO("johndoe@example.com", "wrongpass");
        mockMvc.perform(post("/api/users/loginUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Order(5)
    public void testEditUser() throws Exception {
        long userId = -1L;
    	Optional<User> optionalUser = userRepository.findByEmail("johndoe@example.com");
    	if(optionalUser.isPresent()) {
    		User user = optionalUser.get();
    		userId = user.getId();
    	}
    	LoginDTO loginDTO = new LoginDTO("johndoe@example.com", "pass");
        mockMvc.perform(post("/api/users/loginUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("John Doe"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("USER"));
        ChangeUserDataDTO changeUserDataDTO = new ChangeUserDataDTO("John", "Smith", "johndoe@example.com" ,"pass");
        mockMvc.perform(put("/api/users/editUser/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeUserDataDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("User updated successfully."));
        Optional<User> optionalEditedUser = userRepository.findByEmail("johndoe@example.com");
    	long editedUserId = -2L;
        if(optionalEditedUser.isPresent()) {
    		User editedUser = optionalEditedUser.get();
    		editedUserId = editedUser.getId();
    	}
        assertEquals(userId, editedUserId);
        assertNotEquals(optionalUser.get().getLastName(), optionalEditedUser.get().getLastName());
        assertEquals(optionalEditedUser.get().getLastName(), "Smith");
        userRepository.deleteById(userId);
    }
}
