package com.mumbailocal.trainservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mumbailocal.trainservice.entity.Train;
import com.mumbailocal.trainservice.service.TrainService;

@RestController
@RequestMapping("/train")
public class TrainController {

    @Autowired
    private TrainService trainService;

    // Test API
    @GetMapping("/hello")
    public String hello() {
        return "Train Service is running";
    }

    // Get all trains
    @GetMapping("/all")
    public List<Train> getAllTrains() {
        return trainService.getAllTrains();
    }

    // Get trains by line
    @GetMapping("/by-line")
    public List<Train> getTrainsByLine(@RequestParam String line) {
        return trainService.getTrainsByLine(line);
    }
    
    
}
