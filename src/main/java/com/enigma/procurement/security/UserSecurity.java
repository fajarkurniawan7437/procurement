package com.enigma.procurement.security;

import com.enigma.procurement.entity.Admin;
import com.enigma.procurement.entity.Vendor;
import com.enigma.procurement.service.AdminService;
import com.enigma.procurement.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSecurity {
    private final VendorService vendorService;
    private final AdminService adminService;

    public boolean checkAdmin(Authentication authentication, String adminId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Admin admin = adminService.getById(adminId);
        return admin.getEmail().equals(userDetails.getUsername());
    }

    public boolean checkVendor(Authentication authentication, String vendorId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Vendor vendor = vendorService.getById(vendorId);
        return vendor.getEmail().equals(userDetails.getUsername());
    }
}
