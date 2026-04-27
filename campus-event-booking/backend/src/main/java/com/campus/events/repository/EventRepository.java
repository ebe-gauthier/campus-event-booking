package com.campus.events.repository;

import com.campus.events.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByCategoryIgnoreCase(String category);
    List<Event> findByTitleContainingIgnoreCase(String title);
    List<Event> findByOrganizerIgnoreCase(String organizer);
}
