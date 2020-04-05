package com.postman.calendify.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postman.calendify.models.Slot;

public interface ISlotRepository extends JpaRepository<Slot, Long> {

}
