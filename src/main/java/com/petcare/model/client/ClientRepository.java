package com.petcare.model.client;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    // Búsquedas avanzadas
    List<Client> findByNameContainingIgnoreCase(String name);
}
