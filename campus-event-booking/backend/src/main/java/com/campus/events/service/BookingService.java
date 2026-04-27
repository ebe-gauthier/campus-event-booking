package com.campus.events.service;

import com.campus.events.model.Booking;
import com.campus.events.model.Event;
import com.campus.events.model.Student;
import com.campus.events.repository.BookingRepository;
import com.campus.events.repository.EventRepository;
import com.campus.events.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final EventRepository eventRepository;
    private final StudentRepository studentRepository;
    private final EventService eventService;

    public Booking bookEvent(String studentId, String eventId) {
        if (bookingRepository.existsByStudentIdAndEventId(studentId, eventId)) {
            throw new RuntimeException("You have already booked this event");
        }

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getAvailableSpots() <= 0) {
            throw new RuntimeException("No spots available for this event");
        }

        // Add student to event
        eventService.addStudentToEvent(eventId, studentId);

        // Add event to student's list
        student.getBookedEventIds().add(eventId);
        studentRepository.save(student);

        // Create booking record
        Booking booking = new Booking();
        booking.setStudentId(studentId);
        booking.setStudentName(student.getName());
        booking.setStudentEmail(student.getEmail());
        booking.setEventId(eventId);
        booking.setEventTitle(event.getTitle());

        return bookingRepository.save(booking);
    }

    public void cancelBooking(String studentId, String eventId) {
        Booking booking = bookingRepository.findByStudentIdAndEventId(studentId, eventId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setStatus("CANCELLED");
        bookingRepository.save(booking);

        // Remove from event and student
        eventService.removeStudentFromEvent(eventId, studentId);

        studentRepository.findById(studentId).ifPresent(student -> {
            student.getBookedEventIds().remove(eventId);
            studentRepository.save(student);
        });
    }

    public List<Booking> getBookingsByStudent(String studentId) {
        return bookingRepository.findByStudentId(studentId);
    }

    public List<Booking> getBookingsByEvent(String eventId) {
        return bookingRepository.findByEventId(eventId);
    }
}
