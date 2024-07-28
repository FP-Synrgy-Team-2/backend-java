package com.example.jangkau.repositories.oauth2;

import com.example.jangkau.models.oauth2.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {

    Client findOneByClientId(String clientId);

//    @Modifying
//    @Transactional
//    @Query("UPDATE Client c SET c.accessTokenValiditySeconds = :accessTokenValidity, c.refreshTokenValiditySeconds = :refreshTokenValidity WHERE c.clientId = :clientId")
//    void updateTokenValidity(String clientId, int accessTokenValidity, int refreshTokenValidity);
}

