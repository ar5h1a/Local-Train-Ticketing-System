package com.mumbailocal.ticketservice.service;
import java.time.LocalDate;
import java.math.BigDecimal;

import com.mumbailocal.ticketservice.entity.ProcessedRequest;
import com.mumbailocal.ticketservice.entity.Ticket;
import com.mumbailocal.ticketservice.repository.ProcessedRequestRepository;
import com.mumbailocal.ticketservice.repository.TicketRepository;

import ch.qos.logback.core.model.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import com.mumbailocal.ticketservice.dto.TrainDTO;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;

@Service
public class TicketService {
	@Autowired
	private ProcessedRequestRepository processedRequestRepository;

	
    @Autowired
    private TicketRepository ticketRepository;
    
    @Autowired
    private RestTemplate restTemplate;


    @Value("${train-service.base-url}")
    private String trainServiceBaseUrl;


    // Issue a ticket based on type
    public Ticket issueTicket( String requestId,
    		Long userId,
            String sourceStation,
            String destinationStation,
            String line,
            String type,
            String travelClass,int numberOfPersons) {
		/*
		 * System.out.println("=== DEBUG START ==="); System.out.println("UserId: " +
		 * userId); System.out.println("Line: " + line); System.out.println("Source: '"
		 * + sourceStation + "'"); System.out.println("Destination: '" +
		 * destinationStation + "'"); System.out.println("Type: " + type);
		 * System.out.println("Class: " + travelClass); System.out.println("Persons: " +
		 * numberOfPersons); System.out.println("=== DEBUG END ===");
		 * 
		 * 
		 * //System.out.println("DEBUG type = [" + type + "]");
		 * System.out.println("TYPE RECEIVED = [" + type + "]");
		 */

    	try {
    	    ProcessedRequest processed = new ProcessedRequest(requestId, userId);
    	    processedRequestRepository.save(processed);
    	} catch (Exception e) {
    	    throw new IllegalStateException("Duplicate request detected. Possible replay attack.");
    	}

    	
    	String url = trainServiceBaseUrl + "/train/by-line?line=" + line;
        System.out.println("Calling Train Service URL: " + trainServiceBaseUrl);
        TrainDTO[] train = restTemplate.getForObject(url, TrainDTO[].class);

        if (train == null || train.length == 0) {
            throw new RuntimeException("No trains found for line: " + line);
        }
        
        if (numberOfPersons < 1) {
            throw new IllegalArgumentException("Number of persons must be at least 1");
        }

    	LocalDate now = LocalDate.now();
    	
    	LocalDate validTill;
    	
    	// Prevent duplicate same-day booking for same route
    	if (ticketRepository.existsByUserIdAndSourceStationAndDestinationStationAndValidFrom(
    	        userId,
    	        sourceStation,
    	        destinationStation,
    	        now)) {

    	    throw new IllegalStateException("You have already booked this route today.");
    	}

    
    	switch (type.toLowerCase()) {
        case "single":
            validTill = now;
            break;
        case "return":
            validTill = now.plusDays(1);
            break;
        case "monthly":
            validTill = now.plusDays(30);
            break;
        default:
            throw new IllegalArgumentException("Invalid ticket type");
    }


    	TrainDTO selectedTrain = train[0]; // take first train

    	// Convert stations string to list
    	List<String> stationList = Arrays.stream(selectedTrain.getStations().split(","))
    	                                 .map(String::trim)
    	                                 .toList();

    	int stationsCrossed = calculateStationsCrossed(
    	        stationList,
    	        sourceStation,
    	        destinationStation
    	        
    	        
    	);


    	int price = calculatePrice(stationsCrossed, travelClass);
    	int finalPrice;
    	
    	switch (type.toLowerCase()) {
        case "single":
            finalPrice = price;
            break;
        case "return":
            finalPrice = price * 2;      // Return ticket doubles the price
            break;
        case "monthly":
            finalPrice = price * 30;     // Monthly ticket roughly 30x price
            break;
        default:
            finalPrice = price; // fallback, though already handled above
    }
    	finalPrice = finalPrice * numberOfPersons;

    	
        Ticket ticket = new Ticket();
        ticket.setUserId(userId);
        ticket.setType(type.toUpperCase());
        ticket.setTravelClass(travelClass.toUpperCase());
        ticket.setLine(line);
    	ticket.setPrice(BigDecimal.valueOf(finalPrice));
    	ticket.setNumberOfPersons(numberOfPersons);
        ticket.setValidFrom(now);
        ticket.setValidTill(validTill);
        ticket.setStatus("ACTIVE");
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setSourceStation(sourceStation);
        ticket.setDestinationStation(destinationStation);

        Ticket savedTicket = ticketRepository.save(ticket);

       
     return savedTicket;
     }

    private int calculateStationsCrossed(
            List<String> stations,
            String sourceStation,
            String destinationStation) {

        int sourceIndex = stations.indexOf(sourceStation);
        int destinationIndex = stations.indexOf(destinationStation);
        System.out.println("Stations list: " + stations);
        System.out.println("Source index: " + stations.indexOf(sourceStation));
        System.out.println("Destination index: " + stations.indexOf(destinationStation));


        if (sourceIndex == -1 || destinationIndex == -1) {
        	throw new IllegalArgumentException("Invalid source or destination station");
            
        }

        return Math.abs(destinationIndex - sourceIndex);
    }
    private int calculatePrice(int stationsCrossed, String travelClass) {
    	
    	boolean isFirstClass = "FIRST_CLASS".equalsIgnoreCase(travelClass);

        if (isFirstClass) {

            if (stationsCrossed <= 5) {
                return 25;
            } else if (stationsCrossed <= 10) {
                return 50;
            } else {
                return 85;
            }

        } else {  // SECOND_CLASS

            if (stationsCrossed <= 5) {
                return 5;
            } else if (stationsCrossed <= 10) {
                return 10;
            } else {
                return 15;
            }
        }
        
        
    }

    public List<Ticket> getTicketsByUserFiltered(Long userId, LocalDate fromDate, LocalDate toDate) {
        List<Ticket> tickets = ticketRepository.findByUserId(userId);
        
        return tickets.stream()
                .filter(t -> (fromDate == null || !t.getValidFrom().isBefore(fromDate)) &&
                             (toDate == null || !t.getValidTill().isAfter(toDate)))
                .toList();
    }



    // View ticket history for a user
    public List<Ticket> getTicketsByUser(Long userId) {
        return ticketRepository.findByUserId(userId);

    }
    
    public List<String> getStationsByLine(String line) {

        String url = trainServiceBaseUrl + "/train/by-line?line=" + line;

        TrainDTO[] trains = restTemplate.getForObject(url, TrainDTO[].class);

        if (trains == null || trains.length == 0) {
        	throw new IllegalArgumentException("No trains found for line: " + line);
        }

        return Arrays.stream(trains)
                .flatMap(train ->
                        Arrays.stream(train.getStations().split(",|\\n")))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .toList();
    }
}
