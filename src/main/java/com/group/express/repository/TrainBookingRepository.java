package com.group.express.repository;

import com.group.express.domain.BusBooking;
import com.group.express.domain.Seats;
import com.group.express.domain.TrainBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TrainBookingRepository extends JpaRepository<TrainBooking, String> {

    @Query("SELECT t FROM TrainBooking t WHERE t.member.id = :id")
    List<TrainBooking> findBookingsById(@Param("id") String id);

    @Query("SELECT b FROM TrainBooking b where b.departureStation LIKE %:start% and b.arrivalStation LIKE %:end% and STR_TO_DATE(b.reservationDate, '%Y%m%d') BETWEEN STR_TO_DATE(:startDay, '%Y-%m-%d') AND STR_TO_DATE(:endDay, '%Y-%m-%d')")
    List<TrainBooking> getSearchTrainBookingListall(@Param("start") String start, @Param("end")String end,@Param("startDay") String startDay, @Param("endDay")String endDay);

    @Query("Select b from TrainBooking b where STR_TO_DATE(b.reservationDate,'%Y%m%d') between STR_TO_DATE(:startDay,'%Y-%m-%d') and STR_TO_DATE(:endDay,'%Y-%m-%d')")
    List<TrainBooking> getSearchTrainBookingListDay(@Param("startDay")String startDay, @Param("endDay")String endDay);

    @Override
    Optional<TrainBooking> findById(@Param("ticketNumber") String ticketNumber);

    @Query("Select b from TrainBooking b where b.id=:id and STR_TO_DATE(b.reservationDate,'%Y%m%d') between STR_TO_DATE(:startDay,'%Y-%m-%d') and STR_TO_DATE(:endDay,'%Y-%m-%d')")

    List<TrainBooking> getSearchTrainBookingListDay(@Param("startDay")String startDay, @Param("endDay")String endDay, @Param("id")String id);

    @Query("SELECT b FROM TrainBooking b where b.id=:id and b.departureStation LIKE %:start% and b.arrivalStation LIKE %:end% and STR_TO_DATE(b.reservationDate, '%Y%m%d') BETWEEN STR_TO_DATE(:startDay, '%Y-%m-%d') AND STR_TO_DATE(:endDay, '%Y-%m-%d')")
    List<TrainBooking> getSearchTrainBookingListall(@Param("start") String start, @Param("end")String end,@Param("startDay") String startDay, @Param("endDay")String endDay, @Param("id")String id);


//    @Query("SELECT tb FROM TrainBooking tb WHERE " +
//            "tb.vehicleTypeName = :vehicleTypeName " +
//            "AND tb.departureTime = :departureTime " +
//            "AND tb.arrivalTime = :arrivalTime " +
//            "AND tb.departureStation = :departureStation " +
//            "AND tb.arrivalStation = :arrivalStation " +
//            "AND tb.trainNumber = :trainNumber " +
//            "AND tb.reservationDate = :reservationDate")
//    List<TrainBooking> findBySeat(Seats seat);
}
