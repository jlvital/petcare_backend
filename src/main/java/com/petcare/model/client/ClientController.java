package com.petcare.model.client;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.petcare.model.pet.Pet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/client")
@RequiredArgsConstructor
@Slf4j
public class ClientController {

    private final ClientService clientService;

	    @GetMapping("/dashboard")
    public ResponseEntity<String> getClientDashboard(@AuthenticationPrincipal Client client) {
        if (client == null) {
            return ResponseEntity.status(401).body("No autorizado");
        }
        return ResponseEntity.ok("Bienvenido al panel del cliente, " + client.getName());
    }

    @GetMapping("/pets")
    public ResponseEntity<List<Pet>> getMyPets(@AuthenticationPrincipal Client client) {
        if (client == null) {
            return ResponseEntity.badRequest().build();
        }
        List<Pet> pets = clientService.getPetsOfClient(client);
        return pets.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(pets);
    }

    @PostMapping("/pets")
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet, @AuthenticationPrincipal Client client) {
        if (client == null || pet == null) {
            return ResponseEntity.badRequest().build();
        }
        Pet addPet = clientService.registerPet(client, pet);
        return ResponseEntity.ok(addPet);
    }
}
