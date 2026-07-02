package com.group.express.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.repository.query.Param;

import javax.persistence.Entity;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Seats {

    private String vehicleTypeName;
    private String departureTime;
    private String arrivalTime;
    private String departureStation;
    private String arrivalStation;
    private String trainNumber;
    private String reservationDate;

}
