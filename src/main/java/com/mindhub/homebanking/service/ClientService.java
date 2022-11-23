package com.mindhub.homebanking.service;
import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface ClientService {

    public List<ClientDTO> getClientDTO();

    public ClientDTO findByID(long id);

    public Client findByEmail(String email);

    public void saveClient(Client client);
}