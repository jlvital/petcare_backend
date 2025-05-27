package com.petcare.validations;

import com.petcare.model.client.Client;
import com.petcare.model.pet.Pet;
import com.petcare.model.booking.Booking;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PetValidator {

    /**
     * Verifica si el cliente autenticado es el propietario de la mascota en una cita.
     */
    public static void validateOwnership(Client client, Booking booking) {
        if (client == null || booking == null || booking.getPet() == null || booking.getPet().getClient() == null) {
            log.warn("No se puede validar la propiedad de la mascota: datos incompletos.");
            throw new IllegalArgumentException("No se puede validar la propiedad de la mascota.");
        }

        Long clientId = client.getId();
        Long ownerId = booking.getPet().getClient().getId();

        if (!clientId.equals(ownerId)) {
            log.warn("Cliente ID [{}] intentó acceder a una mascota que no le pertenece (dueño real: {}).", clientId, ownerId);
            throw new IllegalArgumentException("No puedes realizar acciones sobre mascotas que no te pertenecen.");
        }

        log.info("Cliente ID [{}] es el propietario de la mascota ID [{}].", clientId, booking.getPet().getId());
    }

    /**
     * Verifica si el cliente autenticado es dueño de una mascota concreta.
     */
    public static void validateOwnership(Client client, Pet pet) {
        if (client == null || pet == null || pet.getClient() == null) {
            log.warn("No se puede validar la propiedad de la mascota: cliente o mascota sin asignación.");
            throw new IllegalArgumentException("No se puede validar la propiedad de la mascota.");
        }

        Long clientId = client.getId();
        Long ownerId = pet.getClient().getId();

        if (!clientId.equals(ownerId)) {
            log.warn("Cliente ID [{}] intentó acceder a la mascota ID [{}], que pertenece a ID [{}].", clientId, pet.getId(), ownerId);
            throw new IllegalArgumentException("No puedes gestionar una mascota que no te pertenece.");
        }

        log.info("Validación correcta: Cliente ID [{}] es dueño de la mascota ID [{}].", clientId, pet.getId());
    }
}