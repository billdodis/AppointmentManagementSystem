package gr.bookappointment.Appointment.Booking;

import com.fasterxml.jackson.databind.ObjectMapper;

import gr.bookappointment.Appointment.Booking.dto.input.EmployeeInputDTO;
import gr.bookappointment.Appointment.Booking.dto.input.LoginDTO;
import gr.bookappointment.Appointment.Booking.model.Employee;
import gr.bookappointment.Appointment.Booking.repository.EmployeeRepository;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private EmployeeRepository employeeRepository;

    private ObjectMapper objectMapper;
  
    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    @Order(1)
    public void testAddEmployee() throws Exception {
    	EmployeeInputDTO employeeInputDTO = new EmployeeInputDTO("Lionel", "Messi", "lionelmessi@example.com", "pass");
        mockMvc.perform(post("/api/employees/addEmployee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeInputDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Employee created successfully."));
    }
    
    @Test
    @Order(2)
    public void testExistingEmailAddEmployee() throws Exception {
    	EmployeeInputDTO employeeInputDTO = new EmployeeInputDTO("Lionel", "Messi", "lionelmessi@example.com", "pass");
        mockMvc.perform(post("/api/employees/addEmployee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeInputDTO)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Email already exists!"));
    }

    @Test
    @Order(3)
    public void testLoginEmployee() throws Exception {
        LoginDTO loginDTO = new LoginDTO("lionelmessi@example.com", "pass");
        mockMvc.perform(post("/api/employees/loginEmployee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Lionel Messi"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.role").value("EMPLOYEE"));
    }
    
    @Test
    @Order(4)
    public void testLoginEmployeeWrongCredentials() throws Exception {
        LoginDTO loginDTO = new LoginDTO("lionelmessi@example.com", "wrongpass");
        mockMvc.perform(post("/api/employees/loginEmployee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
    
    @Test
    @Order(5)
    public void testGetEmployees() throws Exception {
    	String response = mockMvc.perform(get("/api/employees/getEmployees")
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
    	List<?> employeeList = objectMapper.readValue(response, List.class);
        int initialSize = employeeList.size();
        EmployeeInputDTO employeeInputDTO = new EmployeeInputDTO("Cristiano", "Ronaldo", "cr7@example.com", "pass");
        mockMvc.perform(post("/api/employees/addEmployee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeInputDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string("Employee created successfully."));
        mockMvc.perform(get("/api/employees/getEmployees")
                .contentType(MediaType.APPLICATION_JSON))
        		.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(initialSize + 1))
        		.andExpect(MockMvcResultMatchers.jsonPath("$[" + (initialSize - 1) +"].firstName").value("Lionel"))
        		.andExpect(MockMvcResultMatchers.jsonPath("$[" + (initialSize - 1) +"].lastName").value("Messi"))
        		.andExpect(MockMvcResultMatchers.jsonPath("$[" + (initialSize) +"].firstName").value("Cristiano"))
        		.andExpect(MockMvcResultMatchers.jsonPath("$[" + (initialSize) +"].lastName").value("Ronaldo"));
        Optional<Employee> optionalEmployee = employeeRepository.findByEmail("lionelmessi@example.com");
        long emId = -1L;
        if(optionalEmployee.isPresent()) {
        	Employee employee = optionalEmployee.get();
        	emId = employee.getId();
        }
        employeeRepository.deleteById(emId);
        Optional<Employee> optionalEmployee2 = employeeRepository.findByEmail("cr7@example.com");
        long emId2 = -1L;
        if(optionalEmployee2.isPresent()) {
        	Employee employee2 = optionalEmployee2.get();
        	emId2 = employee2.getId();
        }
        employeeRepository.deleteById(emId2);
        mockMvc.perform(get("/api/employees/getEmployees")
                .contentType(MediaType.APPLICATION_JSON))
        		.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(initialSize - 1));
    }
}