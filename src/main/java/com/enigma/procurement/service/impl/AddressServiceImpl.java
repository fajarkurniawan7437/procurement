package com.enigma.procurement.service.impl;

import com.enigma.procurement.entity.Address;
import com.enigma.procurement.repository.AddressRepository;
import com.enigma.procurement.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    @Override
    public Address create(Address address) {
        return addressRepository.save(address);
    }
}
