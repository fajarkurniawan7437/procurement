package com.enigma.procurement.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class VendorResponse {
    private String vendorId;
    private String name;
    private String mobilePhone;
}
