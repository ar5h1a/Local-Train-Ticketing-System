package com.mumbailocal.trainservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mumbailocal.trainservice.entity.Train;
import com.mumbailocal.trainservice.repository.TrainRepository;

import org.springframework.cache.annotation.Cacheable;

@Service
public class TrainService {

    @Autowired
    private TrainRepository trainRepository;

    // Cache all trains under key "trains"
    @Cacheable(value = "trains")
    public List<Train> getAllTrains() {
        System.out.println("Fetching from DB..."); // to verify caching
        return trainRepository.findAll();
    }

    // Cache single train by id
    @Cacheable(value = "train", key = "#trainId")
    public Train getTrainById(Long trainId) {
        System.out.println("Fetching from DB..."); // to verify caching
        return trainRepository.findById(trainId).orElse(null);
    }
    
    @Cacheable(value = "trainsByLine", key = "#line")
    public List<Train> getTrainsByLine(String line) {
        System.out.println("Fetching trains by line from DB...");
        return trainRepository.findByLine(line);
    }
}
