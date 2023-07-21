package com.enigma.procurement.service;

import com.enigma.procurement.entity.Vendor;

import java.util.List;

public interface VendorService {
    Vendor create(Vendor vendor);

    Vendor getById(String id);

    List<Vendor> getAll();

    Vendor update(Vendor vendor);

    void deleteById(String id);
}
