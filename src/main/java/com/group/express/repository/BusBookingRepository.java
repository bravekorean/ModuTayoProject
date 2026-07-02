package com.group.express.repository;

import com.group.express.domain.BusBooking;
import com.group.express.domain.TrainBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusBookingRepository extends JpaRepository<BusBooking, String>  {

    @Query("SELECT b FROM BusBooking b where b.departureStation LIKE %:start% and b.arrivalStation LIKE %:end% and STR_TO_DATE(b.reservationDate, '%Y%m%d') BETWEEN STR_TO_DATE(:startDay, '%Y-%m-%d') AND STR_TO_DATE(:endDay, '%Y-%m-%d')")
    List<BusBooking> getSearchBusBookingListall(@Param("start") String start, @Param("end")String end,@Param("startDay") String startDay, @Param("endDay")String endDay);


    @Query("SELECT b FROM BusBooking b WHERE b.member.id = :id")
    List<BusBooking> findBookingsById(@Param("id") String id);

    @Query("Select b from BusBooking b where STR_TO_DATE(b.reservationDate,'%Y%m%d') between STR_TO_DATE(:startDay,'%Y-%m-%d') and STR_TO_DATE(:endDay,'%Y-%m-%d')")
    List<BusBooking> getSearchBusBookingListDay(@Param("startDay")String startDay, @Param("endDay")String endDay);
    @Override
    Optional<BusBooking> findById(@Param("ticketNumber") String ticketNumber);

    @Query("Select b from BusBooking b where b.id=:id and STR_TO_DATE(b.reservationDate,'%Y%m%d') between STR_TO_DATE(:startDay,'%Y-%m-%d') and STR_TO_DATE(:endDay,'%Y-%m-%d')")
    List<BusBooking> getSearchBusBookingListDay(@Param("startDay")String startDay, @Param("endDay")String endDay,@Param("id")String id);

    @Query("SELECT b FROM BusBooking b where b.id=:id and b.departureStation LIKE %:start% and b.arrivalStation LIKE %:end% and STR_TO_DATE(b.reservationDate, '%Y%m%d') BETWEEN STR_TO_DATE(:startDay, '%Y-%m-%d') AND STR_TO_DATE(:endDay, '%Y-%m-%d')")
    List<BusBooking> getSearchBusBookingListall(@Param("start") String start, @Param("end")String end,@Param("startDay") String startDay, @Param("endDay")String endDay,@Param("id")String id);

}
