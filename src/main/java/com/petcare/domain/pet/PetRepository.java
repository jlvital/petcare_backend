package com.petcare.domain.pet;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
	
	List<Pet> findByClientId(Long clientId);
	Optional<Pet> findByChipNumber(String chip);
}