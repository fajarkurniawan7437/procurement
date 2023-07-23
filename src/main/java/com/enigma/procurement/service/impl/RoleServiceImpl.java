package com.enigma.procurement.service.impl;

import com.enigma.procurement.entity.Role;
import com.enigma.procurement.entity.constant.ERole;
import com.enigma.procurement.repository.RoleRepository;
import com.enigma.procurement.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public Role getOrSave(ERole role) {
        return roleRepository.findByRole(role).orElseGet(() -> roleRepository.save(Role.builder().role(role).build()));
    }
}
