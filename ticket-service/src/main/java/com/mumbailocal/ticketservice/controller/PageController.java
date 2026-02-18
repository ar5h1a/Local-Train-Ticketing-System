package com.mumbailocal.ticketservice.controller;

import com.mumbailocal.ticketservice.service.TicketService;
import com.mumbailocal.ticketservice.entity.Ticket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PageController {

    @Autowired
    private TicketService ticketService;

    // Redirect root to home
    @GetMapping("/")
    public String root() {
        return "redirect:/home";
    }

    // PAGE 1 – Home
    @GetMapping("/home")
    public String home(@RequestParam(defaultValue = "Western") String line,
                       Model model) {

        List<String> stations = ticketService.getStationsByLine(line);

        model.addAttribute("stations", stations);
        model.addAttribute("selectedLine", line);

        return "home";
    }
    @GetMapping("/changeLine")
    public String changeLine(@RequestParam String line, Model model) {

        List<String> stations = ticketService.getStationsByLine(line);

        model.addAttribute("stations", stations);
        model.addAttribute("selectedLine", line);

        return "home";
    }

    // PAGE 2 – Details
    @PostMapping("/details")
    public String details(@RequestParam String source,
                          @RequestParam String destination,
                          @RequestParam String line,
                          Model model) {

    	if (source == null || destination == null || source.equals(destination)) {

            List<String> stations = ticketService.getStationsByLine(line);

            model.addAttribute("stations", stations);
            model.addAttribute("selectedLine", line);
            model.addAttribute("error",
                    "Source and destination cannot be same");

            return "home";
    	}
    	model.addAttribute("source", source);
        model.addAttribute("destination", destination);
        model.addAttribute("line", line);

        return "details";
    }
    
    // PAGE 3 – Results
    @PostMapping("/results")
    public String results(Authentication authentication,
                          @RequestParam String source,
                          @RequestParam String destination,
                          @RequestParam String line,
                          @RequestParam String type,
                          @RequestParam String travelClass,
                          @RequestParam(defaultValue = "1") int numberOfPersons,
                          Model model) {

        Long userId = Long.parseLong(authentication.getName());

        Ticket ticket = ticketService.issueTicket(
                userId,
                source,
                destination,
                line,
                type,
                travelClass,
                numberOfPersons
        );

        model.addAttribute("ticket", ticket);

        return "results";
    }


    // Profile Page
    @GetMapping("/profile")
    public String profile() {
        return "profile";
    }

    // Booking History
    @GetMapping("/history")
    public String history(Authentication authentication, Model model) {

        Long userId = Long.parseLong(authentication.getName());

        List<Ticket> tickets = ticketService.getTicketsByUser(userId);

        model.addAttribute("tickets", tickets);

        return "history";
    }
    
    @GetMapping("/show-ticket")
    public String showLatestTicket(Authentication authentication, Model model) {

        Long userId = Long.parseLong(authentication.getName());

        // Fetch tickets for the user
        List<Ticket> tickets = ticketService.getTicketsByUser(userId);

        if (tickets.isEmpty()) {
            model.addAttribute("error", "No tickets found");
            return "no-ticket"; // optional page showing "no ticket found"
        }

        // Get the latest ticket (assuming tickets are ordered by creation date)
        Ticket latestTicket = tickets.get(tickets.size() - 1); 

        // Pass the ticket details to the view
        model.addAttribute("ticket", latestTicket);

        return "show-ticket"; // new Thymeleaf template
    }


}
