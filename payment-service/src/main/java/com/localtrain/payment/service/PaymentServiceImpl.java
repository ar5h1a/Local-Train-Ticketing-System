package com.localtrain.payment.service;

import org.springframework.stereotype.Service;
import com.localtrain.payment.entity.Payment;
import com.localtrain.payment.repository.PaymentRepository;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment processPayment(Payment payment) {
        payment.setStatus("SUCCESS");
        payment.setPaymentTime(LocalDateTime.now());
        return paymentRepository.save(payment);
    }
    @Override
    public Payment getPaymentByBookingId(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
    }

}
