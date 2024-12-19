package gr.bookappointment.Appointment.Booking.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import gr.bookappointment.Appointment.Booking.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>{
    List<Appointment> findByDate(LocalDateTime date);
    
    List<Appointment> findByEmid(long emid);
    
    List<Appointment> findByUserid(long userid);
    
}
