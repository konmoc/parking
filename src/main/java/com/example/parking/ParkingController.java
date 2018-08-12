package com.example.parking;

import com.example.parking.entity.DriverType;
import com.example.parking.entity.Visit;
import com.example.parking.entity.VisitStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;

@RestController
public class ParkingController {

    @Autowired
    VisitService visitService;
    @Autowired
    VisitRepository visitRepository;
    //1. As a driver, I want to start the parking meter, so I don’t have to pay the fine for the invalid
    //parking.
    @PostMapping("/visits")
    public Visit createNewVisit(){
        //!dummy test
        Visit visit = new Visit();
        visit.setStartTime(LocalDateTime.now());
        visit.setStatus(VisitStatus.STARTED);
        visit.setDriverType(DriverType.REGULAR);
        visit.setRegistrationPlate("SBI 3107");
        return visitService.saveVisit(visit);
    }

    //2. As a parking operator, I want to check if the vehicle has started the parking meter.
    @GetMapping("/visits/{registrationPlate}")
    public ResponseEntity findVisitByRegistrationPlate(@PathVariable String registrationPlate){
        return visitService.checkIfVisitWasStarted(registrationPlate);
    }

    //3. As a driver, I want to stop the parking meter, so that I pay only for the actual parking time
    @PatchMapping("/visits/{id}")
    public void endVisit(@PathVariable Long id){
        Visit visit = visitService.findVisitById(id);
        visit.setEndTime(LocalDateTime.now());
        visitService.saveVisit(visit);

    }
    //4.As a driver, I want to know how much I have to pay for parking.
    @GetMapping("/visits/{id}/money")
    public ResponseEntity howMuchToPay(@PathVariable Long id){
        return visitService.getMoneyForVisitWithId(id);
    }


    @DeleteMapping("visits/{id}")
    public ResponseEntity deleteVisit(@PathVariable Long id){
        if(visitService.findVisitById(id).equals(null)){
            return new ResponseEntity("No visit found for ID " + id, HttpStatus.NOT_FOUND);
        }
        visitRepository.deleteVisitById(id);
        return new ResponseEntity("Visit deleted for ID " + id, HttpStatus.OK);
    }
    //5. As a parking owner, I want to know how much money I earn any given day.
}
