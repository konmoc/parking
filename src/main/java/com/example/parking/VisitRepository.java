package com.example.parking;

import com.example.parking.entity.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {
    Visit findVisitById(Long id);
    Visit findVisitByRegistrationPlate(String registrationPlate);
    void deleteVisitById(Long id);
    void deleteVisitByRegistrationPlate(String registrationPlate);
    List<Visit> findAll();

}
