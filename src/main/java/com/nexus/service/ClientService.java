package com.nexus.service;

import com.nexus.entities.Client;
import com.nexus.repositories.IClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final IClientRepository clientRepository;

    @Autowired
    public ClientService(IClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    /**
     * Get all clients
     * @return List of all clients
     */
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    /**
     * Get client by id
     * @param id Client id
     * @return Client
     */
    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    /**
     * Create client
     * @param client Client
     * @return Client
     */
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    /**
     * Update client
     * @param id Client id
     * @param clientDetails Client details
     * @return Client
     */
    public Optional<Client> updateClient(Long id, Client clientDetails) {
        return clientRepository.findById(id)
                .map(client -> {
                    client.setName(clientDetails.getName());
                    return clientRepository.save(client);
                });
    }

    /**
     * Delete client
     * @param id Client id
     */
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
