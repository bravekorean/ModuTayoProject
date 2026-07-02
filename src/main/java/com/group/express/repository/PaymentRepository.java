package com.group.express.repository;

import com.group.express.domain.BusBooking;
import com.group.express.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long>  {

    @Query("SELECT p FROM Payment p WHERE p.buyerid = ?1 and p.trainticketNumber IS NOT NULL order by p.paymentid")
    List<Payment> findTrainPaymentsById(String id);

    @Query("SELECT p FROM Payment p WHERE p.buyerid = ?1 and p.busticketNumber IS NOT NULL order by p.paymentid")
    List<Payment> findBusPaymentsById(String id);

    @Query("SELECT p FROM Payment p WHERE p.trainticketNumber IS NOT NULL order by p.paymentid")
    List<Payment> findTrainPayments();

    @Query("SELECT p FROM Payment p WHERE p.busticketNumber IS NOT NULL order by p.paymentid")
    List<Payment> findBusPayments();

    @Query("select p from Payment p where p.payMethod=:paymentType and STR_TO_DATE(p.paymentDate,'%Y%m%d') between STR_TO_DATE(:startDay,'%Y-%m-%d') and STR_TO_DATE(:endDay,'%Y-%m-%d') and p.trainticketNumber IS NOT NULL ")
    List<Payment> getTrainPaymentListByTypeAndDay(@Param("paymentType")String paymentType,@Param("startDay") String startDay, @Param("endDay")String endDay);

    @Query("select p from Payment p where STR_TO_DATE(p.paymentDate,'%Y%m%d') between STR_TO_DATE(:startDay,'%Y-%m-%d') and STR_TO_DATE(:endDay,'%Y-%m-%d') and p.trainticketNumber IS NOT NULL ")
    List<Payment> getTrainPaymentListByDay(@Param("startDay")String startDay,@Param("endDay") String endDay);

    @Query("select p from Payment p where p.payMethod=:paymentType and p.trainticketNumber IS NOT NULL")
    List<Payment> getTrainPaymentListByType(@Param("paymentType")String paymentType);

    @Query("select p from Payment p where p.payMethod=:paymentType and STR_TO_DATE(p.paymentDate,'%Y%m%d') between STR_TO_DATE(:startDay,'%Y-%m-%d') and STR_TO_DATE(:endDay,'%Y-%m-%d') and p.busticketNumber IS NOT NULL ")
    List<Payment> getBusPaymentListByTypeAndDay(@Param("paymentType")String paymentType,@Param("startDay") String startDay, @Param("endDay")String endDay);

    @Query("select p from Payment p where STR_TO_DATE(p.paymentDate,'%Y%m%d') between STR_TO_DATE(:startDay,'%Y-%m-%d') and STR_TO_DATE(:endDay,'%Y-%m-%d') and p.busticketNumber IS NOT NULL ")
    List<Payment> getBusPaymentListByDay(@Param("startDay")String startDay,@Param("endDay") String endDay);

    @Query("select p from Payment p where p.payMethod=:paymentType and p.busticketNumber IS NOT NULL")
    List<Payment> getBusPaymentListByType(@Param("paymentType")String paymentType);

    void deleteByBusticketNumber(String ticketNumber);

    @Query("select p from Payment p where p.buyerid=:id and p.payMethod=:paymentType and STR_TO_DATE(p.paymentDate,'%Y%m%d') between STR_TO_DATE(:startDay,'%Y-%m-%d') and STR_TO_DATE(:endDay,'%Y-%m-%d') and p.trainticketNumber IS NOT NULL ")
    List<Payment> getTrainPaymentListByTypeAndDay(@Param("paymentType")String paymentType,@Param("startDay") String startDay, @Param("endDay") String endDay,@Param("id") String id);

    @Query("select p from Payment p where p.buyerid=:id and STR_TO_DATE(p.paymentDate,'%Y%m%d') between STR_TO_DATE(:startDay,'%Y-%m-%d') and STR_TO_DATE(:endDay,'%Y-%m-%d') and p.trainticketNumber IS NOT NULL ")
    List<Payment> getTrainPaymentListByDay(@Param("startDay")String startDay,@Param("endDay") String endDay,@Param("id") String id);

    @Query("select p from Payment p where p.buyerid=:id and p.payMethod=:paymentType and p.trainticketNumber IS NOT NULL")
    List<Payment> getTrainPaymentListByType(@Param("paymentType")String paymentType,@Param("id") String id);

    @Query("select p from Payment p where p.buyerid=:id and p.payMethod=:paymentType and STR_TO_DATE(p.paymentDate,'%Y%m%d') between STR_TO_DATE(:startDay,'%Y-%m-%d') and STR_TO_DATE(:endDay,'%Y-%m-%d') and p.busticketNumber IS NOT NULL ")
    List<Payment> getBusPaymentListByTypeAndDay(@Param("paymentType")String paymentType,@Param("startDay") String startDay, @Param("endDay")String endDay,@Param("id") String id);

    @Query("select p from Payment p where p.buyerid=:id and STR_TO_DATE(p.paymentDate,'%Y%m%d') between STR_TO_DATE(:startDay,'%Y-%m-%d') and STR_TO_DATE(:endDay,'%Y-%m-%d') and p.busticketNumber IS NOT NULL ")
    List<Payment> getBusPaymentListByDay(@Param("startDay")String startDay,@Param("endDay") String endDay,@Param("id") String id);

    @Query("select p from Payment p where p.buyerid=:id and p.payMethod=:paymentType and p.busticketNumber IS NOT NULL")
    List<Payment> getBusPaymentListByType(@Param("paymentType")String paymentType,@Param("id") String id);
}
