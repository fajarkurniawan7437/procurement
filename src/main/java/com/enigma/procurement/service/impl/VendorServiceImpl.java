package com.enigma.procurement.service.impl;

import com.enigma.procurement.entity.Address;
import com.enigma.procurement.entity.Vendor;
import com.enigma.procurement.model.request.VendorRequest;
import com.enigma.procurement.model.response.VendorResponse;
import com.enigma.procurement.repository.VendorRepository;
import com.enigma.procurement.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
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
    public VendorResponse update(VendorRequest vendor) {
        Vendor currentVendor = getById(vendor.getVendorId());

        if (currentVendor != null) {
            currentVendor.setName(vendor.getVendorName());
            currentVendor.setMobilePhone(vendor.getMobilePhone());

            Address existingAddress = currentVendor.getAddress();

            existingAddress.setCity(vendor.getAddress().getCity());
            existingAddress.setStreet(vendor.getAddress().getStreet());
            existingAddress.setProvince(vendor.getAddress().getProvince());

            Vendor updatedVendor = vendorRepository.save(currentVendor);

            VendorResponse vendorResponse = VendorResponse.builder()
                    .vendorId(updatedVendor.getId())
                    .name(updatedVendor.getName())
                    .mobilePhone(updatedVendor.getMobilePhone())
                    .build();

            return vendorResponse;
        }

        return null;
    }

    @Override
    public void deleteById(String id) {
        Vendor vendor = getById(id);
        vendorRepository.delete(vendor);
    }
}
