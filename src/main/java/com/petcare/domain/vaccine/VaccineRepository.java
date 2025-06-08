package com.petcare.domain.vaccine;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repositorio JPA para gestionar vacunas.
 */
@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Long> {

    /**
     * Busca todas las vacunas aplicadas a una mascota.
     */
    List<Vaccine> findByPetId(Long petId);

    /**
     * Busca las vacunas de una mascota ordenadas por fecha de administración descendente.
     */
    List<Vaccine> findByPetIdOrderByAdministrationDateDesc(Long petId);

    /**
     * Verifica si ya existe una vacuna con un nombre concreto para una mascota.
     */
    boolean existsByNameAndPetId(String name, Long petId);

    /**
     * Busca todas las vacunas cuya fecha de caducidad sea anterior a una fecha dada.
     */
    List<Vaccine> findByExpirationDateBefore(LocalDate date);
}