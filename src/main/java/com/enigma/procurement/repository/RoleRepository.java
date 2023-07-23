package com.enigma.procurement.repository;

import com.enigma.procurement.entity.Role;
import com.enigma.procurement.entity.constant.ERole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByRole(ERole role);
}
