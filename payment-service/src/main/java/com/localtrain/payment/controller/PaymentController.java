package com.localtrain.payment.controller;

import java.time.LocalDateTime;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.localtrain.payment.entity.Payment;
import com.localtrain.payment.service.PaymentService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private Map<String, Payment> tempPaymentMap = new HashMap<>();
    private final RestTemplate restTemplate;
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService, RestTemplate restTemplate) {
        this.paymentService = paymentService;
        this.restTemplate = restTemplate;
    }

    @PostMapping("/pay")
    public Payment makePayment(@RequestBody Payment payment) {
        return paymentService.processPayment(payment);
    }
    
//    private Map<String, Payment> tempPaymentMap = new HashMap<>();

//    @PostMapping("/create-order")
//    public Map<String, Object> createOrder(@RequestParam Double amount) throws Exception {
//
//        RazorpayClient razorpay = new RazorpayClient("rzp_test_S9zcnYb5XxsWxN", "C71fd35o6wsQGK8aTxwYfwrF");
//
//        JSONObject orderRequest = new JSONObject();
//        orderRequest.put("amount", amount * 100);
//        orderRequest.put("currency", "INR");
//        orderRequest.put("receipt", "txn_" + System.currentTimeMillis());
//
//        Order order = razorpay.orders.create(orderRequest);
//
//        Long generatedBookingId = System.currentTimeMillis();
//
//        Payment tempPayment = new Payment();
//        tempPayment.setBookingId(generatedBookingId);
//        tempPayment.setAmount(amount);
//
//        tempPaymentMap.put(order.get("id"), tempPayment);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("id", order.get("id"));
//        response.put("amount", order.get("amount"));
//
//        return response;
//    }
//    
    @PostMapping("/create-order")
    public Map<String, Object> createOrder(@RequestParam Double amount) throws Exception {

        RazorpayClient razorpay =
                new RazorpayClient("rzp_test_S9zcnYb5XxsWxN", "C71fd35o6wsQGK8aTxwYfwrF");

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount * 100); // in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "txn_" + System.currentTimeMillis());

        Order order = razorpay.orders.create(orderRequest);

        // 🔥 Generate bookingId in backend
        Long generatedBookingId = System.currentTimeMillis();

        Payment tempPayment = new Payment();
        tempPayment.setBookingId(generatedBookingId);
        tempPayment.setAmount(amount);

        // Store temporarily using razorpay order id
        tempPaymentMap.put(order.get("id"), tempPayment);

        // 🔥 VERY IMPORTANT → send bookingId to frontend
        Map<String, Object> response = new HashMap<>();
        response.put("id", order.get("id"));
        response.put("amount", order.get("amount"));
        response.put("currency", order.get("currency"));
        response.put("bookingId", generatedBookingId); // ✅ ADD THIS
        
        System.out.println("Order Created: " + order.get("id"));


        return response;
    }

//    @PostMapping("/verify")
//    @ResponseBody
//    public ResponseEntity<String> verifyPayment(
//            @RequestParam String razorpay_order_id,
//            @RequestParam String razorpay_payment_id,
//            @RequestParam String razorpay_signature,
//            @RequestParam Long ticketId,
//            @RequestParam Long userId,
//            @RequestParam Double amount) {
//
//        try {
//
//            // Get the temporary payment object created before opening Razorpay checkout
//            Payment payment = tempPaymentMap.get(razorpay_order_id);
//
//            if (payment == null) {
//                return ResponseEntity.badRequest().body("Invalid Order ID");
//            }
//
//            // Set final payment details
//            payment.setTicketId(ticketId);      // save ticket reference
//            payment.setUserId(userId);          // save user reference
//            payment.setAmount(amount);          // save actual amount paid
//            payment.setPaymentMode("RAZORPAY");
//            payment.setStatus("SUCCESS");
//            payment.setPaymentTime(LocalDateTime.now());
//
//            // Save to DB
//            Payment savedPayment = paymentService.processPayment(payment);
//
//            // Call notification service (optional, won't block payment)
//            try {
//                String notificationUrl = "http://localhost:8086/notification/send"
//                        + "?userId=" + savedPayment.getUserId()
//                        + "&type=ticket_confirmation";
//
//                restTemplate.postForObject(notificationUrl, null, String.class);
//            } catch (Exception ex) {
//                System.out.println("Notification service failed, but payment successful");
//            }
//
//            // Remove from temp map
//            tempPaymentMap.remove(razorpay_order_id);
//
//            // Return bookingId to frontend for redirect
//            return ResponseEntity.ok(savedPayment.getBookingId().toString());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.internalServerError().body("Verification Failed");
//        }
//    }

@PostMapping("/verify")
    @ResponseBody
    public ResponseEntity<String> verifyPayment(
            @RequestParam String razorpay_order_id,
            @RequestParam String razorpay_payment_id,
            @RequestParam String razorpay_signature) {

        try {

            Payment payment = tempPaymentMap.get(razorpay_order_id);

            if (payment == null) {
                return ResponseEntity.badRequest().body("Invalid Order ID");
            }

            payment.setPaymentMode("RAZORPAY");
            payment.setStatus("SUCCESS");
            payment.setPaymentTime(LocalDateTime.now());

            Payment savedPayment = paymentService.processPayment(payment);

            // 🔥 Safe notification call
            try {
                String notificationUrl = "http://localhost:8086/notification/send"
                        + "?userId=" + savedPayment.getBookingId()
                        + "&type=ticket_confirmation";

                restTemplate.postForObject(notificationUrl, null, String.class);
            } catch (Exception ex) {
                System.out.println("Notification service failed, but payment successful");
            }

            tempPaymentMap.remove(razorpay_order_id);

            return ResponseEntity.ok(savedPayment.getBookingId().toString());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Verification Failed");
        }
    }



    @GetMapping("/by-booking/{bookingId}")
    public Payment getPaymentByBooking(@PathVariable Long bookingId) {
        return paymentService.getPaymentByBookingId(bookingId);
    }


}
