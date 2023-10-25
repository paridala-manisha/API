package com.covidTestManagement.covid.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.Query;

import com.covidTestManagement.covid.model.Patient;
import com.covidTestManagement.covid.model.Register;
@Repository
public interface RegisterRepository extends JpaRepository<Register,Integer> {

	@Query("SELECT r FROM Register r WHERE r.email = ?1")
    Optional<Register> findByEmail(String email);
	
	


}