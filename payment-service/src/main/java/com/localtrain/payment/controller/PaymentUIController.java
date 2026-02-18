//package com.localtrain.payment.controller;
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import com.localtrain.payment.entity.Payment;
//import com.localtrain.payment.service.PaymentService;
//
//@Controller
//@RequestMapping("/payments/ui")
//public class PaymentUIController {
//
//    private final PaymentService paymentService;
//
//    public PaymentUIController(PaymentService paymentService) {
//        this.paymentService = paymentService;
//    }
//
//    @GetMapping("/pay")
//    public String showPaymentPage() {
//        return "payment"; // loads payment.html
//    }
//
//    @PostMapping("/pay")
//    public String processPayment(
//            @RequestParam Long bookingId,
//            @RequestParam Double amount,
//            @RequestParam String paymentMode) {
//
//        Payment payment = new Payment();
//        payment.setBookingId(bookingId);
//        payment.setAmount(amount);
//        payment.setPaymentMode(paymentMode);
//
//        paymentService.processPayment(payment);
//
//        return "redirect:/payments/ui/success";
//    }
//
//    @GetMapping("/success")
//    public String successPage() {
//        return "success";
//    }
//}


package com.localtrain.payment.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.localtrain.payment.entity.Payment;
import com.localtrain.payment.service.PaymentService;

@Controller
public class PaymentUIController {

    private final PaymentService paymentService;

    public PaymentUIController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payment")
    public String showPaymentPage(
            @RequestParam Long ticketId,
            @RequestParam Long userId,
            @RequestParam Double amount,
            @RequestParam String sourceStation,
            @RequestParam String destinationStation,
            @RequestParam String travelClass,
            @RequestParam String type,
            Model model) {

        model.addAttribute("ticketId", ticketId);
        model.addAttribute("userId", userId);
        model.addAttribute("amount", amount);

        model.addAttribute("sourceStation", sourceStation);
        model.addAttribute("destinationStation", destinationStation);
        model.addAttribute("travelClass", travelClass);
        model.addAttribute("type", type);

        return "payment";
    }




@GetMapping("/ticket")
    public String showTicketPage(
            @RequestParam Long bookingId,
            @RequestParam String sourceStation,
            @RequestParam String destinationStation,
            @RequestParam String travelClass,
            @RequestParam String type,
            Model model) {

        Payment payment = paymentService.getPaymentByBookingId(bookingId);

        model.addAttribute("payment", payment);
        model.addAttribute("sourceStation", sourceStation);
        model.addAttribute("destinationStation", destinationStation);
        model.addAttribute("travelClass", travelClass);
        model.addAttribute("type", type);

        return "ticket";
    }


}


