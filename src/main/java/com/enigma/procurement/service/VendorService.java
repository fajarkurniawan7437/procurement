package com.enigma.procurement.service;

import com.enigma.procurement.entity.Vendor;
import com.enigma.procurement.model.request.VendorRequest;
import com.enigma.procurement.model.response.VendorResponse;

import java.util.List;

public interface VendorService {
    Vendor create(Vendor vendor);

    Vendor getById(String id);

    List<Vendor> getAll();

    VendorResponse update(VendorRequest vendor);

    void deleteById(String id);
}
