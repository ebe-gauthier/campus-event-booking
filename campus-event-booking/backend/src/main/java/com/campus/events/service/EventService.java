package com.campus.events.service;

import com.campus.events.model.Event;
import com.campus.events.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public Optional<Event> getEventById(String id) {
        return eventRepository.findById(id);
    }

    public List<Event> getEventsByCategory(String category) {
        return eventRepository.findByCategoryIgnoreCase(category);
    }

    public List<Event> searchEvents(String title) {
        return eventRepository.findByTitleContainingIgnoreCase(title);
    }

    public Event createEvent(Event event) {
        return eventRepository.save(event);
    }

    public Optional<Event> updateEvent(String id, Event updatedEvent) {
        return eventRepository.findById(id).map(existing -> {
            existing.setTitle(updatedEvent.getTitle());
            existing.setDescription(updatedEvent.getDescription());
            existing.setLocation(updatedEvent.getLocation());
            existing.setDate(updatedEvent.getDate());
            existing.setCategory(updatedEvent.getCategory());
            existing.setCapacity(updatedEvent.getCapacity());
            existing.setImageUrl(updatedEvent.getImageUrl());
            existing.setOrganizer(updatedEvent.getOrganizer());
            return eventRepository.save(existing);
        });
    }

    public boolean deleteEvent(String id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Event addStudentToEvent(String eventId, String studentId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getRegisteredStudentIds().contains(studentId)) {
            throw new RuntimeException("Student already registered for this event");
        }

        if (event.getAvailableSpots() <= 0) {
            throw new RuntimeException("No available spots for this event");
        }

        event.getRegisteredStudentIds().add(studentId);
        return eventRepository.save(event);
    }

    public Event removeStudentFromEvent(String eventId, String studentId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.getRegisteredStudentIds().remove(studentId);
        return eventRepository.save(event);
    }
}
