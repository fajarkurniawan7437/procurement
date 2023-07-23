package com.enigma.procurement.service;

import com.enigma.procurement.model.request.AuthRequest;
import com.enigma.procurement.model.response.LoginResponse;
import com.enigma.procurement.model.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerVendor(AuthRequest request);
    RegisterResponse registerAdmin(AuthRequest request);
//    RegisterResponse registerSeller(RegisterSellerRequest request);
    LoginResponse login(AuthRequest request);
}
