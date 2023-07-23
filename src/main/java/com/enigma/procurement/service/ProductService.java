package com.enigma.procurement.service;

import com.enigma.procurement.model.request.ProductRequest;
import com.enigma.procurement.model.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductService {
    ProductResponse create(ProductRequest request);
    List<ProductResponse> createBulk(List<ProductRequest> requests);
    ProductResponse getById(String id);
    Page<ProductResponse> getAllByNameOrPrice(String name, Long maxPrice, Integer page, Integer size);
    ProductResponse update(ProductRequest product);

    @Modifying
    @Query("UPDATE Product p SET p.isDeleted = true WHERE p.id = :id")
    void deleteById(String id);
}
