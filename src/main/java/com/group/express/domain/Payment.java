package com.group.express.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "Payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentid;
    private String trainticketNumber;
    private String busticketNumber;
    private String impUid;
    private String merchantUid;
    private int paidAmount;
    private String payMethod;
    private String buyerName;
    private String buyerTel;
    private String buyerid;
    private String paymentDate;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "trainticketNumber", referencedColumnName = "ticketNumber", nullable = false,insertable = false, updatable = false)
    private TrainBooking trainBooking;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "busticketNumber", referencedColumnName = "ticketNumber", nullable = false,insertable = false, updatable = false)
    private BusBooking busBooking;

    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name = "buyerid", referencedColumnName = "id", nullable = false,insertable = false, updatable = false)
    private Member member;
}
