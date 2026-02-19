package com.mumbailocal.ticketservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.mumbailocal.ticketservice.entity.ProcessedRequest;

public interface ProcessedRequestRepository extends JpaRepository<ProcessedRequest, String> {

    boolean existsByRequestId(String requestId);
}
