package com.kth.journalsystem.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentDTO {
    private Long paymentId;
    private BigDecimal amount;
    // Other fields, constructors, getters, setters
}
