package com.example.parking;

import com.example.parking.entity.DriverType;
import com.example.parking.entity.Visit;
import com.example.parking.dto.VisitDTO;
import com.example.parking.entity.VisitStatus;
import com.example.parking.exceptions.VisitCreationException;
import com.example.parking.exceptions.VisitNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/visits")
public class ParkingController {

    @Autowired
    VisitService visitService;
    //1. As a driver, I want to start the parking meter, so I don’t have to pay the fine for the invalid
    //parking.
    @PostMapping
    public Visit createNewVisit() throws VisitCreationException {
        //!dummy test
        Visit visit = new Visit();
        visit.setStartTime(LocalDateTime.now());
        visit.setStatus(VisitStatus.STARTED);
        visit.setDriverType(DriverType.REGULAR);
        visit.setRegistrationPlate("SBI 3107");
        return visitService.saveVisit(visit);
    }

    //2. As a parking operator, I want to check if the vehicle has started the parking meter.
    @GetMapping("/{registrationPlate}")
    public VisitDTO findVisitByRegistrationPlate(@PathVariable String registrationPlate) throws VisitNotFoundException{
        return visitService.getVisitByRegistrationPlate(registrationPlate);
    }

    //3. As a driver, I want to stop the parking meter, so that I pay only for the actual parking time
    @PatchMapping("/{id}")
    public ResponseEntity endVisit(@PathVariable Long id){
        try {
            VisitDTO visitDTO = visitService.endVisit(id);
            return new ResponseEntity("Visit ended",HttpStatus.OK);
        } catch (VisitNotFoundException e) {
            return new ResponseEntity("Visit not found!",HttpStatus.NOT_FOUND);
        }

    }
    //4.As a driver, I want to know how much I have to pay for parking.
    @GetMapping("/{id}/money")
    public ResponseEntity howMuchToPay(@PathVariable Long id){
        try {
            double costs = visitService.getMoneyForVisitWithId(id);
            return new ResponseEntity("Money for visit with id " + id + " = " + costs, HttpStatus.OK);

        } catch (VisitNotFoundException e) {
            return new ResponseEntity("Visit not found!",HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity deleteVisit(@PathVariable Long id){
        if(visitService.findVisitById(id).equals(null)){
            return new ResponseEntity("No visit found for ID " + id, HttpStatus.NOT_FOUND);
        }
        visitService.deleteVisitById(id);
        return new ResponseEntity("Visit deleted for ID " + id, HttpStatus.OK);
    }
    //5. As a parking owner, I want to know how much money I earn any given day.
}
