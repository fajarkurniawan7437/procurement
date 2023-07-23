package com.enigma.procurement.model.request;

import com.enigma.procurement.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class VendorRequest {
    private String vendorId;
    @NotBlank(message = "product name is required")
    private String vendorName;
    @NotBlank(message = "mobile phone name is required")
    private String mobilePhone;
    @NotBlank(message = "address name is required")
    private Address address;
}
