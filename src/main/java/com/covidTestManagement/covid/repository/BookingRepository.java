package com.covidTestManagement.covid.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.covidTestManagement.covid.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Booking findById(int id);
    List<Booking> findByEmail(String email);
}