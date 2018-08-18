package com.example.parking.dto;

import com.example.parking.entity.DriverType;
import com.example.parking.entity.VisitStatus;
import lombok.Data;



import java.time.LocalDateTime;
@Data
public class VisitDTO {
    private Long id;
    private String registrationPlate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double moneyToBePaid;
    private DriverType driverType;
    private VisitStatus status;


    private Double calculateMoneyForVisit(){

    }
}
