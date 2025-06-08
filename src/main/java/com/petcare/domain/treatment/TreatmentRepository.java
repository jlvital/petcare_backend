package com.petcare.domain.treatment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petcare.enums.TreatmentStatus;

/**
 * Repositorio JPA para gestionar el acceso a los tratamientos registrados.
 * Permite realizar consultas por mascota, estado y orden cronológico.
 */
@Repository
public interface TreatmentRepository extends JpaRepository<Treatment, Long> {

    /**
     * Recupera todos los tratamientos asociados a una mascota concreta.
     *
     * @param petId ID de la mascota
     * @return lista de tratamientos registrados
     */
    List<Treatment> findByPetId(Long petId);

    /**
     * Recupera todos los tratamientos de una mascota ordenados por fecha de inicio descendente.
     * Los tratamientos más recientes aparecerán primero.
     *
     * @param petId ID de la mascota
     * @return lista de tratamientos ordenada
     */
    List<Treatment> findByPetIdOrderByStartDateDesc(Long petId);

    /**
     * Recupera todos los tratamientos de una mascota con un estado específico.
     *
     * @param petId  ID de la mascota
     * @param status estado del tratamiento (por ejemplo: EN_CURSO, FINALIZADO)
     * @return lista de tratamientos filtrados por estado
     */
    List<Treatment> findByPetIdAndStatus(Long petId, TreatmentStatus status);

    /**
     * Recupera el tratamiento más reciente registrado para una mascota,
     * ordenado por fecha de inicio descendente.
     *
     * @param petId ID de la mascota
     * @return el tratamiento más reciente, o null si no existen tratamientos
     */
    Treatment findTopByPetIdOrderByStartDateDesc(Long petId);
}