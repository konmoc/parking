package com.example.parking;

import com.example.parking.entity.DriverType;
import com.example.parking.entity.Visit;
import com.example.parking.dto.VisitDTO;
import com.example.parking.entity.VisitStatus;
import com.example.parking.exceptions.VisitNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VisitService {

    @Autowired
    VisitRepository visitRepository;

    public Visit saveVisit(Visit visit) {
        return visitRepository.save(visit);
    }

    public Visit findVisitById(Long id) {
        return visitRepository.findVisitById(id);
    }

    public Visit findVisitByRegistrationPlate(String registrationPlate) {
        return visitRepository.findVisitByRegistrationPlate(registrationPlate);
    }

    public void deleteVisitById(Long id) {
        visitRepository.deleteVisitById(id);
    }

    public void deleteVisitByRegistrationPlate(String registrationPlate) {
        visitRepository.deleteVisitByRegistrationPlate(registrationPlate);
    }

    public double getMoneyForVisitWithId(Long id) throws VisitNotFoundException {

        return Optional.ofNullable(visitRepository.findVisitById(id))
                .map(this::calculateCostsForVisit)
                .orElseThrow(() -> new VisitNotFoundException(id));
    }

//        public ResponseEntity checkIfVisitWasStarted (String registrationNumber){
//            if (findVisitByRegistrationPlate(registrationNumber).getStatus().equals(VisitStatus.STARTED)) {
//                return new ResponseEntity("Visit found for register number " + registrationNumber, HttpStatus.OK);
//            } else {
//                return new ResponseEntity("No visit found for register number " + registrationNumber, HttpStatus.NOT_FOUND);
//            }
//        }

    public VisitDTO getVisitByRegistrationPlate(String registrationPlate) throws VisitNotFoundException {
        return Optional.ofNullable(visitRepository.findVisitByRegistrationPlate(registrationPlate))
                .map(this::convertToDto)
                .orElseThrow(() -> new VisitNotFoundException(registrationPlate));
    }

    public VisitDTO endVisit(@PathVariable Long id) throws VisitNotFoundException{
        return Optional.ofNullable(visitRepository.findVisitById(id))
                .map(visit -> { visit.setEndTime(LocalDateTime.now());
                                visit.setStatus(VisitStatus.ENDED);
                                saveVisit(visit);
                                return convertToDto(visit);
                })
                .orElseThrow(() -> new VisitNotFoundException(id));
    }


    private int numofStartedHours(Visit visit) {
        LocalDateTime start = visit.getStartTime();
        LocalDateTime now = LocalDateTime.now();
        return (int) Duration.between(start, now).toHours() + 1;
    }

    private double calculateCostsForVisit(Visit visit) {
        int numOfStartedHours = numofStartedHours(visit);
        double cost;
        if (visit.getDriverType().equals(DriverType.REGULAR)) {
            if (numOfStartedHours == 1) {
                cost = 1;
            } else if (numOfStartedHours == 2) {
                cost = 2;
            } else {
                cost = 3 + 3 * (1 - Math.pow(1.5, numOfStartedHours - 2)) / (-0.5);
            }
        } else {
            if (numOfStartedHours == 1) {
                cost = 0;
            } else if (numOfStartedHours == 2) {
                cost = 2;
            } else {
                cost = 2 + 3 * (1 - Math.pow(1.2, numOfStartedHours - 2)) / (-0.2);
            }
        }
        return cost;
    }

    private VisitDTO convertToDto(Visit visit) {
        ModelMapper modelMapper = new ModelMapper();
        VisitDTO visitDTO = modelMapper.map(visit, VisitDTO.class);
        return visitDTO;
    }

}


