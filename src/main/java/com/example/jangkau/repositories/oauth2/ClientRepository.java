package com.example.jangkau.repositories.oauth2;

import com.example.jangkau.models.oauth2.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {

    Client findOneByClientId(String clientId);

}

