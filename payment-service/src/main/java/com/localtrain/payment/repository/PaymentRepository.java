package com.localtrain.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.localtrain.payment.entity.Payment;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByBookingId(Long bookingId);

}
