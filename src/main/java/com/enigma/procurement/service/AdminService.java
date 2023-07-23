package com.enigma.procurement.service;

import com.enigma.procurement.entity.Admin;
import com.enigma.procurement.model.response.AdminResponse;

public interface AdminService {
    Admin create(Admin admin);

    Admin getById(String id);
}
