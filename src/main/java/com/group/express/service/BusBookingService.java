package com.group.express.service;

import com.group.express.domain.BusBooking;
import com.group.express.domain.TrainBooking;
import com.group.express.repository.BusBookingRepository;
import com.group.express.repository.TrainBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BusBookingService {
    private final BusBookingRepository busBookingRepository;
    @Autowired
    BusBookingService(BusBookingRepository busBookingRepository){this.busBookingRepository=busBookingRepository;}

    public List<BusBooking> getBusBookingList(String id ){
        return busBookingRepository.findBookingsById(id);
    }

    public List<BusBooking> getBusBookingList( ){
        return busBookingRepository.findAll();
    }

    public List<BusBooking> getSearchBusBookingListall(String start,String end,String startDay,String endDay){
        return busBookingRepository.getSearchBusBookingListall(start,end,startDay,endDay);
    }
    public List<BusBooking> getSearchBusBookingListDay(String startDay,String endDay){
        return busBookingRepository.getSearchBusBookingListDay(startDay,endDay);
    }
    public List<BusBooking> getSearchBusBookingListall(String start,String end,String startDay,String endDay,String id){
        return busBookingRepository.getSearchBusBookingListall(start,end,startDay,endDay,id);
    }
    public List<BusBooking> getSearchBusBookingListDay(String startDay,String endDay,String id){
        return busBookingRepository.getSearchBusBookingListDay(startDay,endDay,id);
    }
    public BusBooking getUsedMileage(String ticketNumber) {

       return busBookingRepository.findById(ticketNumber).orElse(null);
    }

    public void busTicketSuccess(BusBooking busBooking) throws Exception {
        // 결제 정보를 데이터베이스에 저장
            busBookingRepository.save(busBooking);
    }


    @Transactional
    public void deleteBusBooking(String ticketNumber) {
        busBookingRepository.deleteById(ticketNumber);
    }
}
