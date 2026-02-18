package com.localtrain.payment.service;

import com.localtrain.payment.entity.Payment;

public interface PaymentService {
    Payment processPayment(Payment payment);
    Payment getPaymentByBookingId(Long bookingId);

}
