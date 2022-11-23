package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client, Long> {
    // una clase hereda de otra, una clase hereda de una interfaz. Una interfaz hereda de otra pero una interfaz no hereda de una clase.
    Client findByEmail(String email);
}
