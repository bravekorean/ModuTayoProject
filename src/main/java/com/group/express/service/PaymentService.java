package com.group.express.service;

import com.group.express.domain.BusBooking;
import com.group.express.domain.Payment;
import com.group.express.repository.BusBookingRepository;
import com.group.express.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;
    @Autowired
    public PaymentService(PaymentRepository paymentRepository){this.paymentRepository=paymentRepository;}

    public List<Payment> getTrainPaymentList(String id ){
        return paymentRepository.findTrainPaymentsById(id);
    }
    public List<Payment> getBusPaymentList(String id ){
        return paymentRepository.findBusPaymentsById(id);
    }
    public List<Payment> getTrainPaymentList(){
        return paymentRepository.findTrainPayments();
    }
    public List<Payment> getBusPaymentList(){
        return paymentRepository.findBusPayments();
    }


    public void PaymentSuccess(Payment payment) {
        // 결제 정보를 데이터베이스에 저장
        try {
            paymentRepository.save(payment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Payment> getTrainPaymentListByTypeAndDay(String paymentType, String startDay, String endDay) {
        return paymentRepository.getTrainPaymentListByTypeAndDay(paymentType,startDay,endDay);
    }

    public List<Payment> getTrainPaymentListByDay(String startDay, String endDay) {
        return paymentRepository.getTrainPaymentListByDay(startDay,endDay);
    }

    public List<Payment> getTrainPaymentListByType(String paymentType) {
        return paymentRepository.getTrainPaymentListByType(paymentType);
    }

    public List<Payment> getBusPaymentListByTypeAndDay(String paymentType, String startDay, String endDay) {
        return paymentRepository.getBusPaymentListByTypeAndDay(paymentType,startDay,endDay);
    }

    public List<Payment> getBusPaymentListByDay(String startDay, String endDay) {
        return paymentRepository.getBusPaymentListByDay(startDay,endDay);
    }

    public List<Payment> getBusPaymentListByType(String paymentType) {
        return paymentRepository.getBusPaymentListByType(paymentType);
    }

    public void deleteBusBooking(String ticketNumber) {
        paymentRepository.deleteByBusticketNumber(ticketNumber);
    }

    public List<Payment> getTrainPaymentListByTypeAndDay(String paymentType, String startDay, String endDay, String id) {
        return paymentRepository.getTrainPaymentListByTypeAndDay(paymentType,startDay,endDay,id);
    }

    public List<Payment> getTrainPaymentListByDay(String startDay, String endDay, String id) {
        return paymentRepository.getTrainPaymentListByDay(startDay,endDay,id);
    }

    public List<Payment> getTrainPaymentListByType(String paymentType, String id) {
        return paymentRepository.getTrainPaymentListByType(paymentType,id);
    }
    public List<Payment> getBusPaymentListByTypeAndDay(String paymentType, String startDay, String endDay, String id) {
        return paymentRepository.getBusPaymentListByTypeAndDay(paymentType,startDay,endDay,id);
    }

    public List<Payment> getBusPaymentListByDay(String startDay, String endDay, String id) {
        return paymentRepository.getBusPaymentListByDay(startDay,endDay,id);
    }

    public List<Payment> getBusPaymentListByType(String paymentType, String id) {
        return paymentRepository.getBusPaymentListByType(paymentType,id);
    }

}
