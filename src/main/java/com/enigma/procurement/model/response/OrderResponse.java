package com.enigma.procurement.model.response;

import com.enigma.procurement.entity.Admin;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class OrderResponse {
    private String orderId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transDate;
    private AdminResponse admin;
    private List<OrderDetailResponse> orderDetails;
    private String createdBy;
}
