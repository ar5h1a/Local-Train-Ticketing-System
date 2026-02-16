package com.mumbailocal.trainservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mumbailocal.trainservice.entity.Train;
import java.util.List;

public interface TrainRepository extends JpaRepository<Train, Long> {

    // Custom query method for later use
    List<Train> findByLine(String line);
}
