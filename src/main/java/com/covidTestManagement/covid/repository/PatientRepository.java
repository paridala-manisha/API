package com.covidTestManagement.covid.repository;

import java.util.logging.Logger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.covidTestManagement.covid.model.Patient;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Patient findByEmail(String email);

    // Create a logger
    static final Logger logger = Logger.getLogger(PatientRepository.class.getName());
}