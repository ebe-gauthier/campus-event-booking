package com.campus.events.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI campusEventsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Campus Event Booking API")
                        .description("""
                                REST API for the Campus Event Booking System.
                                
                                ## Endpoints
                                - **Events** – Create, read, update, delete campus events
                                - **Students** – Register and authenticate students
                                - **Bookings** – Book and cancel event registrations
                                
                                ## How to use
                                1. Register a student via `POST /api/students/register`
                                2. Login via `POST /api/students/login` to get your student ID
                                3. Create events via `POST /api/events`
                                4. Book an event via `POST /api/bookings`
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Campus Dev Team")
                                .email("dev@campus.edu"))
                        .license(new License()
                                .name("MIT License")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Local Development Server")
                ));
    }
}
