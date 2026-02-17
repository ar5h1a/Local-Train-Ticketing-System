package com.mumbailocal.trainservice.entity;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "trains")
public class Train implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "train_id")
    private Long trainId;

    @Column(name = "name")
    private String name;

    @Column(name = "line")
    private String line;

    
    @Column(name = "stations")
    private String stations;


    @Column(name = "frequency")
    private Integer frequency;

    @Column(name = "first_departure")
    private String firstDeparture;

    @Column(name = "last_departure")
    private String lastDeparture;

    // Getters and Setters
    public Long getTrainId() {
        return trainId;
    }

    public void setTrainId(Long trainId) {
        this.trainId = trainId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getStations() {
        return stations;
    }

    public void setStations(String stations) {
        this.stations = stations;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getFirstDeparture() {
        return firstDeparture;
    }

    public void setFirstDeparture(String firstDeparture) {
        this.firstDeparture = firstDeparture;
    }

    public String getLastDeparture() {
        return lastDeparture;
    }

    public void setLastDeparture(String lastDeparture) {
        this.lastDeparture = lastDeparture;
    }
}
