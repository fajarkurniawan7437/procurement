package com.enigma.procurement.controller;

import com.enigma.procurement.entity.Category;
import com.enigma.procurement.entity.Vendor;
import com.enigma.procurement.model.request.ProductRequest;
import com.enigma.procurement.model.response.CommonResponse;
import com.enigma.procurement.model.response.ProductResponse;
import com.enigma.procurement.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v2/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @PreAuthorize("hasRole('VENDOR') and @userSecurity.checkVendor(authentication, #category)")
    public ResponseEntity<?> createNewCategory(@RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<Category>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully create new category")
                        .data(categoryService.create(category))
                        .build());
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDOR')")
    public ResponseEntity<?> getAllCategory() {
        List<Category> categories = categoryService.getAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all category")
                        .data(categories)
                        .build());
    }

}
