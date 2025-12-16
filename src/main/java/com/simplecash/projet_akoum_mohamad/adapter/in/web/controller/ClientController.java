package com.simplecash.projet_akoum_mohamad.adapter.in.web.controller;

import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.ClientDTO;
import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.CreateClientRequest;
import com.simplecash.projet_akoum_mohamad.adapter.in.web.dto.UpdateClientRequest;
import com.simplecash.projet_akoum_mohamad.adapter.in.web.mapper.ClientWebMapper;
import com.simplecash.projet_akoum_mohamad.application.port.in.ClientUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clients")
public class ClientController {
    
    private final ClientUseCase clientUseCase;
    
    public ClientController(ClientUseCase clientUseCase) {
        this.clientUseCase = clientUseCase;
    }
    
    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        List<ClientDTO> clients = clientUseCase.getAllClients().stream()
                .map(ClientDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(clients);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        ClientDTO client = new ClientDTO(clientUseCase.getClientById(id));
        return ResponseEntity.ok(client);
    }
    
    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody CreateClientRequest request) {
        ClientDTO client = new ClientDTO(clientUseCase.createClient(
                ClientWebMapper.toCommand(request)
        ));
        return ResponseEntity.status(HttpStatus.CREATED).body(client);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ClientDTO> updateClient(
            @PathVariable Long id,
            @RequestBody UpdateClientRequest request) {
        ClientDTO client = new ClientDTO(clientUseCase.updateClient(
                id,
                ClientWebMapper.toCommand(request)
        ));
        return ResponseEntity.ok(client);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientUseCase.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}

