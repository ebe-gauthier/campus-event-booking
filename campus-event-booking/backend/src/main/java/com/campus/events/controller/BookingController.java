package com.campus.events.controller;

import com.campus.events.model.Booking;
import com.campus.events.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<?> bookEvent(@RequestBody Map<String, String> request) {
        try {
            String studentId = request.get("studentId");
            String eventId = request.get("eventId");
            Booking booking = bookingService.bookEvent(studentId, eventId);
            return ResponseEntity.status(HttpStatus.CREATED).body(booking);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<?> cancelBooking(@RequestBody Map<String, String> request) {
        try {
            String studentId = request.get("studentId");
            String eventId = request.get("eventId");
            bookingService.cancelBooking(studentId, eventId);
            return ResponseEntity.ok(Map.of("message", "Booking cancelled successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Booking>> getBookingsByStudent(@PathVariable String studentId) {
        return ResponseEntity.ok(bookingService.getBookingsByStudent(studentId));
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Booking>> getBookingsByEvent(@PathVariable String eventId) {
        return ResponseEntity.ok(bookingService.getBookingsByEvent(eventId));
    }
}
