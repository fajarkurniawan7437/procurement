package com.enigma.procurement.repository;

import com.enigma.procurement.entity.UserCrendential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialRepository extends JpaRepository<UserCrendential, String> {
    Optional<UserCrendential> findByEmail(String email);
}
