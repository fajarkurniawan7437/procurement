package com.enigma.procurement.service;

import com.enigma.procurement.entity.Vendor;

public interface VendorService {
    Vendor create(Vendor vendor);

    Vendor getById(String id);

    Vendor update(Vendor vendor);

    void deleteById(String id);
}
