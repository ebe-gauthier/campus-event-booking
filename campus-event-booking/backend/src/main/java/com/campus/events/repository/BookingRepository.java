package com.campus.events.repository;

import com.campus.events.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends MongoRepository<Booking, String> {
    List<Booking> findByStudentId(String studentId);
    List<Booking> findByEventId(String eventId);
    Optional<Booking> findByStudentIdAndEventId(String studentId, String eventId);
    boolean existsByStudentIdAndEventId(String studentId, String eventId);
}
