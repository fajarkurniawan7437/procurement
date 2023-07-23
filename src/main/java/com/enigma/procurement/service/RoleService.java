package com.enigma.procurement.service;

import com.enigma.procurement.entity.Role;
import com.enigma.procurement.entity.constant.ERole;

public interface RoleService {
    Role getOrSave(ERole role);
}
