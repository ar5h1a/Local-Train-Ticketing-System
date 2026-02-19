package com.mumbailocal.ticketservice.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "processed_requests")
public class ProcessedRequest {

    @Id
    @Column(name = "request_id", nullable = false, unique = true)
    private String requestId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public ProcessedRequest() {}

    public ProcessedRequest(String requestId, Long userId) {
        this.requestId = requestId;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
    }

    public String getRequestId() {
        return requestId;
    }

    public Long getUserId() {
        return userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
