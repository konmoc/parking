package com.example.parking;

import com.example.parking.entity.DriverType;
import com.example.parking.entity.Visit;
import com.example.parking.VisitRepository;
import com.example.parking.entity.VisitStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class VisitService {

    @Autowired
    VisitRepository visitRepository;

    public Visit saveVisit(Visit visit){return visitRepository.save(visit);}
    public Visit findVisitById(Long id){return visitRepository.findVisitById(id);}
    public Visit findVisitByRegistrationPlate(String registrationPlate){return visitRepository.findVisitByRegistrationPlate(registrationPlate);}
    public void deleteVisitById(Long id){visitRepository.deleteVisitById(id);}
    public void deleteVisitByRegistrationPlate(String registrationPlate){visitRepository.deleteVisitByRegistrationPlate(registrationPlate);}
    public ResponseEntity getMoneyForVisitWithId(Long id) {
        if(visitRepository.findVisitById(id).equals(null)){
            return new ResponseEntity("No visit found for ID " + id, HttpStatus.NOT_FOUND);
        }else{
            Visit visit = findVisitById(id);
            LocalDateTime start = visit.getStartTime();
            LocalDateTime now = LocalDateTime.now();
            int numOfStartedHours = (int) Duration.between(start,now).toHours()+1;

            if(visit.getDriverType().equals(DriverType.REGULAR)){
                if(numOfStartedHours==1){
                    return new ResponseEntity(1, HttpStatus.OK);
                }else if(numOfStartedHours==2){
                    return new ResponseEntity(2, HttpStatus.OK);
                }else{
                    return new ResponseEntity(3+3*(1-Math.pow(1.5,numOfStartedHours-2))/(-0.5),HttpStatus.OK);
                }
            }else {
                if (numOfStartedHours == 1) {
                    return new ResponseEntity(0, HttpStatus.OK);
                } else if (numOfStartedHours == 2) {
                    return new ResponseEntity(2, HttpStatus.OK);
                } else {
                    return new ResponseEntity(2 + 3 * (1 - Math.pow(1.2, numOfStartedHours - 2)) / (-0.2), HttpStatus.OK);
                }
            }
        }

    }

    public ResponseEntity checkIfVisitWasStarted(String registrationNumber){
        if(findVisitByRegistrationPlate(registrationNumber).getStatus().equals(VisitStatus.STARTED)){
            return new ResponseEntity("Visit found for register number " + registrationNumber, HttpStatus.OK);
        }else{
            return new ResponseEntity("No visit found for register number " + registrationNumber, HttpStatus.NOT_FOUND);
        }
    }
}

