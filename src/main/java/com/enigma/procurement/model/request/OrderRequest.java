package com.enigma.procurement.model.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderRequest {
    private String adminId;
    private List<OrderDetailRequest> orderDetails;
}
