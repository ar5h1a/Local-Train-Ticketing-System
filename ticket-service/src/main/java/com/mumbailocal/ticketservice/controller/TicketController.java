package com.mumbailocal.ticketservice.controller;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.mumbailocal.ticketservice.entity.Ticket;
import com.mumbailocal.ticketservice.service.TicketService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;
    
    @GetMapping("/my/filter")
    public ResponseEntity<List<Ticket>> getMyTicketsFiltered(
            Authentication authentication,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

    	Long userId = Long.parseLong(authentication.getName());
        List<Ticket> tickets = ticketService.getTicketsByUserFiltered(userId, fromDate, toDate);
        return ResponseEntity.ok(tickets);
    }

    @PostMapping("/issue")
    public ResponseEntity<Ticket> issueTicket(
            @RequestHeader("X-Request-Id") String requestId,
            @RequestParam String sourceStation,
            @RequestParam String destinationStation,
            @RequestParam String line,
            @RequestParam String type,
            @RequestParam String travelClass,
            @RequestParam int numberOfPersons,Authentication authentication) {
    	if (sourceStation == null || sourceStation.isBlank())
            throw new IllegalArgumentException("Source station is required");

        if (destinationStation == null || destinationStation.isBlank())
            throw new IllegalArgumentException("Destination station is required");

        if (sourceStation.equalsIgnoreCase(destinationStation))
            throw new IllegalArgumentException("Source and destination cannot be same");

        if (numberOfPersons <= 0)
            throw new IllegalArgumentException("Number of persons must be greater than 0");
    		
        Long userId = Long.parseLong(authentication.getName());


        Ticket ticket = ticketService.issueTicket(
        		requestId,
                userId,
                sourceStation,
                destinationStation,
                line,
                type,
                travelClass,
                numberOfPersons
        );
        return ResponseEntity.status(201).body(ticket);

    }


    // Get ticket history
    @GetMapping("/my")
    public List<Ticket> getMyTickets(Authentication authentication) {
        Long userId = Long.parseLong(authentication.getName());
        return ticketService.getTicketsByUser(userId);
    }

    @GetMapping("/health")
    public String health() {
        return "Ticket Service is running";
    }
    
   


}
