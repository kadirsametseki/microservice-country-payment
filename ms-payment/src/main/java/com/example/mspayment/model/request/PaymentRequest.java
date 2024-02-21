package com.example.mspayment.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentRequest {

    @Min(value = 1)
    @Max(value = 1000)
    BigDecimal amount;
    @NotBlank(message = "Description cannot be empty")
    String description;
    @NotBlank(message = "Currency cannot be empty")
    String currency;
}
