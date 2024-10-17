package com.nexus.server.controllers;

import com.nexus.server.entities.Client;
import com.nexus.server.services.ClientService;
import com.nexus.server.utils.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/clients") // http://localhost:8081/api/clients
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Get all clients
     * @return List of all clients
     * @route GET /api/clients
     */
    @GetMapping
    public List<Client> getAllClients() {
        return clientService.getAllClients();
    }

    /**
     * Get client by id
     * @param id Client id
     * @return Client
     * @route GET /api/clients/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id '" + id + "' not found")));
    }

    /**
     * Create client - Request body:
     * <pre>
     *     {
     *      "name": "Client N°",
     *      "email": "email@mail.com",
     *      "address": "1234 Street",
     *      "phone": "1234567890",
     *     }
     * </pre>
     * @param client Client
     * @return Client
     * @route POST /api/clients
     */
    @PostMapping
    public ResponseEntity<Client> createClient(@Valid @RequestBody Client client) {
        return ResponseEntity.ok(clientService.createClient(client));
    }

    /**
     * Update client - Request body:
     * @param id Client id
     * @param clientDetails Client details
     * @return Client
     * @route PUT /api/clients/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @Valid @RequestBody Client clientDetails) {
        return ResponseEntity.ok(clientService.updateClient(id, clientDetails)
                .orElseThrow(() -> new ResourceNotFoundException("Client with id '" + id + "' not found")));
    }

    /**
     * Delete client
     * @param id Client id
     * @route DELETE /api/clients/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteClient(@PathVariable Long id) {
        if(clientService.getClientById(id).isEmpty()) {
            throw new ResourceNotFoundException("Client with id '" + id + "' not found");
        }

        clientService.deleteClient(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Client deleted successfully");
        return ResponseEntity.ok(response);
    }
}
