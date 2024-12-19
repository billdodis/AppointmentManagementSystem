package gr.bookappointment.Appointment.Booking;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import gr.bookappointment.Appointment.Booking.dto.input.AppointmentInputDTO;
import gr.bookappointment.Appointment.Booking.model.Appointment;
import gr.bookappointment.Appointment.Booking.repository.AppointmentRepository;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppointmentControllerTest {
	@Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private AppointmentRepository apRepository;

    private ObjectMapper objectMapper;
      
    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    @Order(1)
    @Transactional
    public void testAddAppointment() throws Exception {
    	LocalDateTime newAppointmentDate = LocalDateTime.of(2060, 1, 2, 10, 30, 0);
    	// In order to run the Appointment controller tests properly,
    	// every appointment addition MUST have user and employee IDs
    	// that EXIST on the database!!!
    	AppointmentInputDTO appointmentInputDTO = new AppointmentInputDTO(91L, 34L, newAppointmentDate);
        mockMvc.perform(post("/api/appointments/addAppointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentInputDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());
    }
    
    @Test
    @Order(2)
    @Transactional
    public void testGetTimeslots() throws Exception {
    	String response = mockMvc.perform(get("/api/employees/getEmployees")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
    	List<?> employeeList = objectMapper.readValue(response, List.class);
        int numOfEmployees = employeeList.size();
        LocalDateTime thisAppointmentDate = LocalDateTime.of(2060, 1, 2, 11, 00, 0);
    	AppointmentInputDTO appointmentInputDTO = new AppointmentInputDTO(91L, 34L, thisAppointmentDate);
    	mockMvc.perform(post("/api/appointments/addAppointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentInputDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());
    	mockMvc.perform(get("/api/appointments/getTimeslots")
    			.param("date", thisAppointmentDate.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentInputDTO)))
        		.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].time", is("11:00:00")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[4].employeeList", hasSize(numOfEmployees - 1)));
    	}

    @Test
    @Order(3)
    @Transactional
    public void testGetAppointmentsByEmId() throws Exception {
    	String response = mockMvc.perform(get("/api/appointments/getAppointmentsByEmId/{emid}", 34L)
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(MockMvcResultMatchers.status().isOk())
    			.andReturn()
    			.getResponse()
    			.getContentAsString();
    	List<?> initialAppointments = objectMapper.readValue(response, List.class);
    	int initialAppointmentsSize = initialAppointments.size();
    	LocalDateTime newAppointmentDate = LocalDateTime.of(2060, 1, 2, 11, 30, 0);
    	AppointmentInputDTO appointmentInputDTO = new AppointmentInputDTO(91L, 34L, newAppointmentDate);
    	mockMvc.perform(post("/api/appointments/addAppointment")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(appointmentInputDTO)))
 				 .andExpect(MockMvcResultMatchers.status().isCreated());
    	String secondResponse = mockMvc.perform(get("/api/appointments/getAppointmentsByEmId/{emid}", 34L)
 	            .contentType(MediaType.APPLICATION_JSON))
 	            .andExpect(MockMvcResultMatchers.status().isOk())
 	            .andReturn()
 	            .getResponse()
 	            .getContentAsString();
    	List<?> finalAppointments = objectMapper.readValue(secondResponse, List.class);
    	int finalAppointmentsSize = finalAppointments.size();
    	assertEquals(initialAppointmentsSize + 1, finalAppointmentsSize);
    }
    
    @Test
    @Order(4)
    @Transactional
    public void testGetAppointmentsByUserId() throws Exception {
    	String response = mockMvc.perform(get("/api/appointments/getAppointmentsByUserId/{userid}", 91L)
    			.contentType(MediaType.APPLICATION_JSON))
    			.andExpect(MockMvcResultMatchers.status().isOk())
    			.andReturn()
    			.getResponse()
    			.getContentAsString();
    	List<?> initialAppointments = objectMapper.readValue(response, List.class);
    	int initialAppointmentsSize = initialAppointments.size();
    	LocalDateTime newAppointmentDate = LocalDateTime.of(2060, 1, 2, 12, 0, 0);
    	AppointmentInputDTO appointmentInputDTO = new AppointmentInputDTO(91L, 34L, newAppointmentDate);
    	mockMvc.perform(post("/api/appointments/addAppointment")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(objectMapper.writeValueAsString(appointmentInputDTO)))
 				 .andExpect(MockMvcResultMatchers.status().isCreated());
    	String secondResponse = mockMvc.perform(get("/api/appointments/getAppointmentsByUserId/{userid}", 91L)
 	            .contentType(MediaType.APPLICATION_JSON))
 	            .andExpect(MockMvcResultMatchers.status().isOk())
 	            .andReturn()
 	            .getResponse()
 	            .getContentAsString();
    	List<?> finalAppointments = objectMapper.readValue(secondResponse, List.class);
    	int finalAppointmentsSize = finalAppointments.size();
    	assertEquals(initialAppointmentsSize + 1, finalAppointmentsSize);
    }
    
    @Test
    @Order(5)
    @Transactional
    public void testGetAppointments() throws Exception {
    	String response = mockMvc.perform(get("/api/appointments/getAppointments")
    			.contentType(MediaType.APPLICATION_JSON))
    			.andReturn()
    			.getResponse()
    			.getContentAsString();
    	List<?>	initialAppointments = objectMapper.readValue(response, List.class);
        int initialAppointmentsSize = initialAppointments.size();
        LocalDateTime newAppointmentDate = LocalDateTime.of(2060, 1, 2, 12, 30, 0);
    	AppointmentInputDTO appointmentInputDTO = new AppointmentInputDTO(91L, 34L, newAppointmentDate);
    	mockMvc.perform(post("/api/appointments/addAppointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentInputDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());        
    	String secondResponse = mockMvc.perform(get("/api/appointments/getAppointments")
                .contentType(MediaType.APPLICATION_JSON))
        		.andReturn()
 	            .getResponse()
 	            .getContentAsString();
    	List<?> finalAppointments = objectMapper.readValue(secondResponse, List.class);
    	int finalAppointmentsSize = finalAppointments.size();
    	assertEquals(initialAppointmentsSize + 1, finalAppointmentsSize);
        List<Appointment> optionalAppointment = apRepository.findByDate(newAppointmentDate);
        apRepository.deleteById(optionalAppointment.get(optionalAppointment.size()-1).getId());
        assertEquals(initialAppointmentsSize, apRepository.count());
    }
    
    @Test
    @Order(6)
    @Transactional
    public void testDeleteAppointmentById() throws Exception {
    	String response = mockMvc.perform(get("/api/appointments/getAppointments")
    			.contentType(MediaType.APPLICATION_JSON))
    			.andReturn()
    			.getResponse()
    			.getContentAsString();
    	List<?>	initialAppointments = objectMapper.readValue(response, List.class);
        int initialAppointmentsSize = initialAppointments.size();
        LocalDateTime newAppointmentDate = LocalDateTime.of(2060, 1, 2, 13, 0, 0);
    	AppointmentInputDTO appointmentInputDTO = new AppointmentInputDTO(91L, 34L, newAppointmentDate);
    	mockMvc.perform(post("/api/appointments/addAppointment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(appointmentInputDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    	String secondResponse = mockMvc.perform(get("/api/appointments/getAppointments")
    			.contentType(MediaType.APPLICATION_JSON))
    			.andReturn()
    			.getResponse()
    			.getContentAsString();
    	List<?>	addedAppointments = objectMapper.readValue(secondResponse, List.class);
        int addedAppointmentsSize = addedAppointments.size();
    	assertEquals(initialAppointmentsSize + 1, addedAppointmentsSize);
    	List<Appointment> optionalAppointment = apRepository.findByDate(newAppointmentDate);
        long id = optionalAppointment.get(optionalAppointment.size()-1).getId();
    	mockMvc.perform(delete("/api/appointments/deleteAppointmentById/{id}", id)
    			.contentType(MediaType.APPLICATION_JSON))
               	.andExpect(MockMvcResultMatchers.status().isOk());
    	String finalResponse = mockMvc.perform(get("/api/appointments/getAppointments")
    			.contentType(MediaType.APPLICATION_JSON))
    			.andReturn()
    			.getResponse()
    			.getContentAsString();
    	List<?>	finalAppointments = objectMapper.readValue(finalResponse, List.class);
        int finalAppointmentsSize = finalAppointments.size();
        assertEquals(initialAppointmentsSize, finalAppointmentsSize);
        assertEquals(apRepository.existsById(id), false);
    }
}
