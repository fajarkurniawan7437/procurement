package com.enigma.procurement.service.impl;

import com.enigma.procurement.entity.*;
import com.enigma.procurement.model.request.OrderRequest;
import com.enigma.procurement.model.response.*;
import com.enigma.procurement.repository.OrderRepository;
import com.enigma.procurement.service.AdminService;
import com.enigma.procurement.service.OrderService;
import com.enigma.procurement.service.ProductPriceService;
import com.enigma.procurement.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductPriceService productPriceService;
    private final AdminService adminService;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public OrderResponse createNewTransaction(OrderRequest request) {

        Admin admin = adminService.getById(request.getAdminId());

        List<OrderDetail> orderDetails = request.getOrderDetails().stream().map(orderDetailRequest -> {
            ProductPrice productPrice = productPriceService.getById(orderDetailRequest.getProductPriceId());

            return OrderDetail.builder()
                    .productPrice(productPrice)
                    .quantity(orderDetailRequest.getQuantity())
                    .build();
        }).collect(Collectors.toList());

        Order order = Order.builder()
                .transDate(LocalDateTime.now())
                .orderDetails(orderDetails)
                .build();
        orderRepository.saveAndFlush(order);

        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream().map(orderDetail -> {
            orderDetail.setOrder(order);

            ProductPrice currentProductPrice = orderDetail.getProductPrice();
            currentProductPrice .setStock(currentProductPrice.getStock() - orderDetail.getQuantity());

            return OrderDetailResponse.builder()
                    .orderDetailId(orderDetail.getId())
                    .quantity(orderDetail.getQuantity())
                    .product(ProductResponse.builder()
                            .productId(currentProductPrice.getProduct().getId())
                            .productName(currentProductPrice.getProduct().getName())
                            .code(currentProductPrice.getProduct().getCode())
                            .category(Category.builder()
                                    .id(currentProductPrice.getProduct().getCategory().getId())
                                    .name(currentProductPrice.getProduct().getCategory().getName())
                                    .build())
                            .price(currentProductPrice.getPrice())
                            .stock(currentProductPrice.getStock())
                            .vendor(VendorResponse.builder()
                                    .vendorId(currentProductPrice.getVendor().getId())
                                    .name(currentProductPrice.getVendor().getName())
                                    .mobilePhone(currentProductPrice.getVendor().getMobilePhone())
                                    .build())
                            .createdAt(currentProductPrice.getProduct().getCreatedAt())
                            .updatedAt(currentProductPrice.getProduct().getUpdatedAt())
                            .build())
                    .build();
        }).collect(Collectors.toList());

        AdminResponse adminResponse = AdminResponse.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .build();

        return OrderResponse.builder()
                .orderId(order.getId())
                .admin(adminResponse)
                .transDate(order.getTransDate())
                .orderDetails(orderDetailResponses)
                .build();
    }

    @Override
    public OrderResponse getOrderById(String id) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "order not found"));

        List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream().map(orderDetail -> {
            orderDetail.setOrder(order);
            ProductPrice currentProductPrice = orderDetail.getProductPrice();

            return OrderDetailResponse.builder()
                    .orderDetailId(orderDetail.getId())
                    .quantity(orderDetail.getQuantity())
                    .product(ProductResponse.builder()
                            .productId(currentProductPrice.getProduct().getId())
                            .productName(currentProductPrice.getProduct().getName())
                            .code(currentProductPrice.getProduct().getCode())
                            .category(Category.builder()
                                    .id(currentProductPrice.getProduct().getCategory().getId())
                                    .name(currentProductPrice.getProduct().getCategory().getName())
                                    .build())
                            .price(currentProductPrice.getPrice())
                            .stock(currentProductPrice.getStock())
                            .vendor(VendorResponse.builder()
                                    .vendorId(currentProductPrice.getVendor().getId())
                                    .name(currentProductPrice.getVendor().getName())
                                    .mobilePhone(currentProductPrice.getVendor().getMobilePhone())
                                    .build())
                            .createdAt(currentProductPrice.getProduct().getCreatedAt())
                            .updatedAt(currentProductPrice.getProduct().getUpdatedAt())
                            .build())
                    .build();
        }).collect(Collectors.toList());

        Admin admin = order.getAdmin();
        AdminResponse adminResponse = AdminResponse.builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .build();
        return OrderResponse.builder()
                .orderId(order.getId())
                .admin(adminResponse)
                .transDate(order.getTransDate())
                .orderDetails(orderDetailResponses)
                .build();
    }


    @Override
    public List<OrderResponse> getAllTransaction(String vendorName) {
        Specification<Order> specification= (root, query, criteriaBuilder) -> {
            Join<Order, Vendor> vendor = root.join("vendor");
            if (vendorName != null) {
                Predicate name = criteriaBuilder.like(criteriaBuilder.lower(vendor.get("name")), vendorName.toLowerCase() + "%");
                return query.where(name).getRestriction();
            }

            return query.getRestriction();
        };

        List<Order> orders = orderRepository.findAll(specification);

        return orders.stream().map(order -> {
            List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails().stream().map(orderDetail -> {
                orderDetail.setOrder(order);
                ProductPrice currentProductPrice = orderDetail.getProductPrice();

                return OrderDetailResponse.builder()
                        .orderDetailId(orderDetail.getId())
                        .quantity(orderDetail.getQuantity())
                        .product(ProductResponse.builder()
                                .productId(currentProductPrice.getProduct().getId())
                                .productName(currentProductPrice.getProduct().getName())
                                .code(currentProductPrice.getProduct().getCode())
                                .category(Category.builder()
                                        .id(currentProductPrice.getProduct().getCategory().getId())
                                        .name(currentProductPrice.getProduct().getCategory().getName())
                                        .build())
                                .price(currentProductPrice.getPrice())
                                .stock(currentProductPrice.getStock())
                                .vendor(VendorResponse.builder()
                                        .vendorId(currentProductPrice.getVendor().getId())
                                        .name(currentProductPrice.getVendor().getName())
                                        .mobilePhone(currentProductPrice.getVendor().getMobilePhone())
                                        .build())
                                .createdAt(currentProductPrice.getProduct().getCreatedAt())
                                .updatedAt(currentProductPrice.getProduct().getUpdatedAt())
                                .build())
                        .build();
            }).collect(Collectors.toList());

            Admin admin = order.getAdmin();
            AdminResponse adminResponse = AdminResponse.builder()
                    .id(admin.getId())
                    .email(admin.getEmail())
                    .build();
            return OrderResponse.builder()
                    .orderId(order.getId())
                    .admin(adminResponse)
                    .transDate(order.getTransDate())
                    .orderDetails(orderDetailResponses)
                    .build();
        }).collect(Collectors.toList());
    }
}
