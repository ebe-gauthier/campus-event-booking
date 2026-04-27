package com.campus.events.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "events")
public class Event {

    @Id
    private String id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Date is required")
    private LocalDateTime date;

    @NotBlank(message = "Category is required")
    private String category;

    private int capacity;

    private String imageUrl;

    private String organizer;

    private List<String> registeredStudentIds = new ArrayList<>();

    private LocalDateTime createdAt = LocalDateTime.now();

    public int getAvailableSpots() {
        return capacity - registeredStudentIds.size();
    }
}
