package com.group.express.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "TrainBooking")
public class TrainBooking {

    private String vehicleTypeName;
    private String departureTime;
    private String arrivalTime;
    private String departureStation;
    private String arrivalStation;
    private int fare;
    private String trainNumber;
    private String seatNumber;
    private int trainCarNumber;
    @Id
    private String ticketNumber;
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