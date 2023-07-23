package com.enigma.procurement.controller;

import com.enigma.procurement.entity.Vendor;
import com.enigma.procurement.model.request.VendorRequest;
import com.enigma.procurement.model.response.CommonResponse;
import com.enigma.procurement.model.response.VendorResponse;
import com.enigma.procurement.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v2/vendor")
public class VendorController {
    private final VendorService vendorService;

    @PostMapping
    @PreAuthorize("hasRole('VENDOR') and @userSecurity.checkVendor(authentication, #vendor)")
    public ResponseEntity<?> createNewVendor(@RequestBody Vendor vendor) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<Vendor>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully create new vendor")
                        .data(vendorService.create(vendor))
                        .build());
    }

    @GetMapping(path = "{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDOR')")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<Vendor>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully get vendor by id")
                        .data(vendorService.getById(id))
                        .build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> getAllVendor() {
        List<Vendor> vendors = vendorService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all vendor")
                        .data(vendors)
                        .build());
    }

    @PutMapping
    @PreAuthorize("hasRole('VENDOR') and @userSecurity.checkVendor(authentication, #vendor.getVendorId())")
    public ResponseEntity<CommonResponse<VendorResponse>> updateVendor(@RequestBody VendorRequest vendor) {
        VendorResponse updatedVendor = vendorService.update(vendor);

        if (updatedVendor != null) {
            CommonResponse<VendorResponse> commonResponse = CommonResponse.<VendorResponse>builder()
                    .statusCode(HttpStatus.OK.value())
                    .message("Successfully update customer")
                    .data(updatedVendor)
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(commonResponse);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasRole('VENDOR') and @userSecurity.checkVendor(authentication, #id)")
    public ResponseEntity<?> deleteVendor(@PathVariable String id) {
        vendorService.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<String>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully delete vendor")
                        .build());
    }
}
