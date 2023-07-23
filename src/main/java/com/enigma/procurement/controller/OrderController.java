package com.enigma.procurement.controller;

import com.enigma.procurement.model.request.OrderRequest;
import com.enigma.procurement.model.response.CommonResponse;
import com.enigma.procurement.model.response.OrderResponse;
import com.enigma.procurement.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/transactions")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') and @userSecurity.checkVendor(authentication, #request.getAdminId())")
    public ResponseEntity<?> createNewTransaction(@RequestBody OrderRequest request) {
        OrderResponse orderResponse = orderService.createNewTransaction(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Successfully create new transaction")
                        .data(orderResponse)
                        .build());
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasRole('ADMIN') and @userSecurity.checkVendor(authentication, #id)")
    public ResponseEntity<?> getTransactionById(@PathVariable String id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get transaction by id")
                        .data(order)
                        .build());
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') and @userSecurity.checkVendor(authentication, #adminName)")
    public ResponseEntity<?> getTransactions(@RequestParam(name = "adminName", required = false) String adminName) {
        List<OrderResponse> orderResponses = orderService.getAllTransaction(adminName);
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Successfully get all transaction")
                        .data(orderResponses)
                        .build());
    }
}
