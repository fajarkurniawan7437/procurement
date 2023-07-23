package com.enigma.procurement.service;

import com.enigma.procurement.model.request.OrderRequest;
import com.enigma.procurement.model.response.OrderResponse;

import java.util.List;

public interface OrderService {

    OrderResponse createNewTransaction(OrderRequest request);
    OrderResponse getOrderById(String id);
    List<OrderResponse> getAllTransaction(String vendorName);
}
