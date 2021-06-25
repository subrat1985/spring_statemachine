package com.subrat.statemachine.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue
    private Long id;
    @Enumerated(EnumType.STRING)
    private PaymentState state;
    private BigDecimal amount;
}
