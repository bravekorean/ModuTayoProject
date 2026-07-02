package com.group.express.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "BusBooking")
public class BusBooking {



    private String routeId;
    private String busClass;
    private String departureTime;
    private String arrivalTime;
    @Id
    private String ticketNumber;
    private String departureStation;
    private String arrivalStation;
    private String seatNumber;
    private int fare;
    private String id;
    private String name;
    private String reservationDate;
    private int usedMileage;
    @JsonProperty("Party")
    @Column(name = "Party")
    private int Party;


    @ManyToOne
    @OnDelete(action= OnDeleteAction.CASCADE)
    @JoinColumn(name = "id", referencedColumnName = "id", nullable = false,insertable = false, updatable = false)
    private Member member;

}

