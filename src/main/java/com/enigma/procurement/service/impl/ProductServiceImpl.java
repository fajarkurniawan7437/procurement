package com.enigma.procurement.service.impl;

import com.enigma.procurement.entity.*;
import com.enigma.procurement.model.request.ProductRequest;
import com.enigma.procurement.model.response.ProductResponse;
import com.enigma.procurement.model.response.VendorResponse;
import com.enigma.procurement.repository.ProductRepository;
import com.enigma.procurement.service.*;
import com.enigma.procurement.specification.ProductSpecification;
import com.enigma.procurement.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductPriceService productPriceService;
    private final VendorService vendorService;
    private final ValidationUtil validationUtil;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductResponse create(ProductRequest request) {
        validationUtil.validate(request);
        Category category = categoryService.getById(request.getCategoryId());
        Vendor vendor = vendorService.getById(request.getVendorId());

        Product product = Product.builder()
                .code(request.getCode())
                .name(request.getProductName())
                .category(category)
                .isDeleted(false)
                .build();
        productRepository.saveAndFlush(product);

        ProductPrice productPrice = ProductPrice.builder()
                .price(request.getPrice())
                .stock(request.getStock())
                .product(product)
                .vendor(vendor)
                .isActive(true)
                .build();
        productPriceService.create(productPrice);

        return ProductResponse.builder()
                .productId(product.getId())
                .code(product.getCode())
                .productName(product.getName())
                .category(Category.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build())
                .price(productPrice.getPrice())
                .stock(productPrice.getStock())
                .vendor(VendorResponse.builder()
                        .vendorId(vendor.getId())
                        .name(vendor.getName())
                        .mobilePhone(vendor.getMobilePhone())
                        .build())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .createdBy(product.getCreatedBy())
                .updatedBy(product.getUpdatedBy())
                .build();
    }

    @Override
    public List<ProductResponse> createBulk(List<ProductRequest> requests) {
        return requests.stream().map(this::create).collect(Collectors.toList());
    }

    @Override
    public ProductResponse getById(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
        Optional<ProductPrice> productPrice = product.getProductPrices().stream().filter(ProductPrice::getIsActive).findFirst();

        if (productPrice.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found");
        Category category = productPrice.get().getProduct().getCategory();
        Vendor vendor = productPrice.get().getVendor();

        return toProductResponse(product, productPrice.get(), category, vendor);
    }

    @Override
    public Page<ProductResponse> getAllByNameOrPrice(String name, Long maxPrice, Integer page, Integer size) {
        Specification<Product> specification = ProductSpecification.getSpecification(name, maxPrice);
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productRepository.findAll(specification, pageable);
        List<ProductResponse> productResponses = new ArrayList<>();
        for (Product product : products.getContent()) {
            Optional<ProductPrice> productPrice = product.getProductPrices()
                    .stream()
                    .filter(ProductPrice::getIsActive).findFirst();

            if (productPrice.isEmpty()) continue;
            Category category = productPrice.get().getProduct().getCategory();
            Vendor vendor = productPrice.get().getVendor();

            productResponses.add(toProductResponse(product, productPrice.get(), category, vendor));
        }

        return new PageImpl<>(productResponses, pageable, products.getTotalElements());
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductResponse update(ProductRequest request) {
        Product currentProduct = findByIdOrThrowNotFound(request.getProductId());
        currentProduct.setName(request.getProductName());
        currentProduct.setCode(request.getCode());

        ProductPrice productPriceActive = productPriceService.findProductPriceActive(request.getProductId(), true);

        if (!productPriceActive.getProduct().getCategory().getId().equals(request.getCategoryId()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "category tidak boleh diubah");

        if (!request.getPrice().equals(productPriceActive.getPrice())) {
            productPriceActive.setIsActive(false);
            ProductPrice productPrice = productPriceService.create(ProductPrice.builder()
                    .price(request.getPrice())
                    .stock(request.getStock())
                    .product(currentProduct)
                    .isActive(true)
                    .build());
            currentProduct.addProductPrice(productPrice);
            return toProductResponse(currentProduct, productPrice, productPrice.getProduct().getCategory(), productPrice.getVendor());
        }

        productPriceActive.setStock(request.getStock());

        return toProductResponse(currentProduct, productPriceActive, productPriceActive.getProduct().getCategory(), productPriceActive.getVendor());
    }

    @Override
    public void deleteById(String id) {
        Product product = findByIdOrThrowNotFound(id);
        productRepository.delete(product);
    }

    private Product findByIdOrThrowNotFound(String id) {
        return productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found"));
    }

    private static ProductResponse toProductResponse(Product product, ProductPrice productPrice, Category category, Vendor vendor) {
        return ProductResponse.builder()
                .productId(product.getId())
                .code(product.getCode())
                .productName(product.getName())
                .category(Category.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build())
                .vendor(VendorResponse.builder()
                        .vendorId(vendor.getId())
                        .name(vendor.getName())
                        .mobilePhone(vendor.getMobilePhone())
                        .build())
                .price(productPrice.getPrice())
                .stock(productPrice.getStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .createdBy(product.getCreatedBy())
                .updatedBy(product.getUpdatedBy())
                .build();
    }
}
