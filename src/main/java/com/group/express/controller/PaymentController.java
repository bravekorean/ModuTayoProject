package com.group.express.controller;

import com.group.express.domain.BusBooking;
import com.group.express.domain.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.group.express.service.PaymentService;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService PaymentService;

    @Autowired
    private PaymentController(PaymentService PaymentService){this.PaymentService=PaymentService;}

    @GetMapping("/PaymentBus/{id}")
    public ResponseEntity<List<Payment>> getPaymentBus(@PathVariable String id) {
        return ResponseEntity.ok(PaymentService.getBusPaymentList(id));}

    @GetMapping("/PaymentBus_admin")
    public ResponseEntity<List<Payment>> getPaymentBus() {
        return ResponseEntity.ok(PaymentService.getBusPaymentList());}
    @GetMapping("/PaymentTrain_admin")
    public ResponseEntity<List<Payment>> getPaymentTrain() {
        return ResponseEntity.ok(PaymentService.getTrainPaymentList());}
    @GetMapping("/PaymentTrain/{id}")
    public ResponseEntity<List<Payment>> getPaymentTrain(@PathVariable String id) {
        return ResponseEntity.ok(PaymentService.getTrainPaymentList(id));}

    @PostMapping("/getPaymentinfo")
    public ResponseEntity<String> handlePaymentSuccess(@RequestBody Payment payment) {
        try {
            PaymentService.PaymentSuccess(payment);
            return ResponseEntity.ok("Payment success");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment failed");
        }
    }



    @GetMapping("/Train_searchFilter")
    public ResponseEntity<List<Payment>> getListMember(@RequestParam String id, @RequestParam(required = false) String paymentType
            , @RequestParam(required = false) String startDay, @RequestParam(required = false) String endDay){
        List<Payment> PaymentList=null;
        if(!paymentType.isEmpty()&&!startDay.isEmpty()&&!endDay.isEmpty()){
            PaymentList=PaymentService.getTrainPaymentListByTypeAndDay(paymentType,startDay,endDay);
        }else if(paymentType.isEmpty()&&!startDay.isEmpty()&&!endDay.isEmpty()){
            PaymentList=PaymentService.getTrainPaymentListByDay(startDay,endDay);
        }else if(!paymentType.isEmpty()&&startDay.isEmpty()&&endDay.isEmpty()) {
            PaymentList=PaymentService.getTrainPaymentListByType(paymentType);
        }

            return ResponseEntity.ok(PaymentList);
        }

    @GetMapping("/Bus_searchFilter")
    public ResponseEntity<List<Payment>> getBusPaymentListSearch(@RequestParam String id, @RequestParam(required = false) String paymentType
            , @RequestParam(required = false) String startDay, @RequestParam(required = false) String endDay){
        List<Payment> PaymentList=null;
        if(!paymentType.isEmpty()&&!startDay.isEmpty()&&!endDay.isEmpty()){
            PaymentList=PaymentService.getBusPaymentListByTypeAndDay(paymentType,startDay,endDay);
        }else if(paymentType.isEmpty()&&!startDay.isEmpty()&&!endDay.isEmpty()){
            PaymentList=PaymentService.getBusPaymentListByDay(startDay,endDay);
        }else if(!paymentType.isEmpty()&&startDay.isEmpty()&&endDay.isEmpty()) {
            PaymentList=PaymentService.getBusPaymentListByType(paymentType);
        }

        return ResponseEntity.ok(PaymentList);
    }

    @DeleteMapping("/delete/{ticketNumber}")
    public ResponseEntity<?> deleteBusBooking(@PathVariable String ticketNumber){
        PaymentService.deleteBusBooking(ticketNumber);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/Train_searchFilter_user")
    public ResponseEntity<List<Payment>> getListMember_user(@RequestParam String id, @RequestParam(required = false) String paymentType
            , @RequestParam(required = false) String startDay, @RequestParam(required = false) String endDay){
        List<Payment> PaymentList=null;
        if(!paymentType.isEmpty()&&!startDay.isEmpty()&&!endDay.isEmpty()){
            PaymentList=PaymentService.getTrainPaymentListByTypeAndDay(paymentType,startDay,endDay,id);
        }else if(paymentType.isEmpty()&&!startDay.isEmpty()&&!endDay.isEmpty()){
            PaymentList=PaymentService.getTrainPaymentListByDay(startDay,endDay,id);
        }else if(!paymentType.isEmpty()&&startDay.isEmpty()&&endDay.isEmpty()) {
            PaymentList=PaymentService.getTrainPaymentListByType(paymentType,id);
        }

        return ResponseEntity.ok(PaymentList);
    }

    @GetMapping("/Bus_searchFilter_user")
    public ResponseEntity<List<Payment>> getBusPaymentListSearch_user(@RequestParam String id, @RequestParam(required = false) String paymentType
            , @RequestParam(required = false) String startDay, @RequestParam(required = false) String endDay){
        List<Payment> PaymentList=null;
        if(!paymentType.isEmpty()&&!startDay.isEmpty()&&!endDay.isEmpty()){
            PaymentList=PaymentService.getBusPaymentListByTypeAndDay(paymentType,startDay,endDay,id);
        }else if(paymentType.isEmpty()&&!startDay.isEmpty()&&!endDay.isEmpty()){
            PaymentList=PaymentService.getBusPaymentListByDay(startDay,endDay,id);
        }else if(!paymentType.isEmpty()&&startDay.isEmpty()&&endDay.isEmpty()) {
            PaymentList=PaymentService.getBusPaymentListByType(paymentType,id);
        }

        return ResponseEntity.ok(PaymentList);
    }






}





