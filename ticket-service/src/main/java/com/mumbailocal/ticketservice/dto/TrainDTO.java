package com.mumbailocal.ticketservice.dto;

import java.util.List;

public class TrainDTO {
	    private int trainId;
	    private String name;
	    private String line;
	    private String stations;   // 👈 CHANGE THIS
	    private String firstDeparture;
	    private String lastDeparture;
	    private int frequency;
		public int getTrainId() {
			return trainId;
		}
		public void setTrainId(int trainId) {
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
		public int getFrequency() {
			return frequency;
		}
		public void setFrequency(int frequency) {
			this.frequency = frequency;
		}
	}



