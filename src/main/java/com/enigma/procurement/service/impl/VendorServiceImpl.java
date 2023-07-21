package com.enigma.procurement.service.impl;

import com.enigma.procurement.entity.Vendor;
import com.enigma.procurement.repository.VendorRepository;
import com.enigma.procurement.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;

    @Override
    public Vendor create(Vendor vendor) {
        try {
            return vendorRepository.save(vendor);
        }catch (DataIntegrityViolationException exception){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Mobile Phone already used");
        }
    }

    @Override
    public Vendor getById(String id) {
        return vendorRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "vendor not found"));
    }

    @Override
    public List<Vendor> getAll() {
        return vendorRepository.findAll();
    }

    @Override
    public Vendor update(Vendor vendor) {
        Vendor currentVendor = getById(vendor.getId());

        if (currentVendor != null) {
            return vendorRepository.save(vendor);
        }

        return null;
    }

    @Override
    public void deleteById(String id) {
        Vendor vendor = getById(id);
        vendorRepository.delete(vendor);
    }
}
