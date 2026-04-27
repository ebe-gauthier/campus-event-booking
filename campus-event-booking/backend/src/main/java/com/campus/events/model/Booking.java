package com.campus.events.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "bookings")
public class Booking {

    @Id
    private String id;

    private String studentId;
    private String studentName;
    private String studentEmail;

    private String eventId;
    private String eventTitle;

    private LocalDateTime bookedAt = LocalDateTime.now();

    private String status = "CONFIRMED"; // CONFIRMED, CANCELLED
}
