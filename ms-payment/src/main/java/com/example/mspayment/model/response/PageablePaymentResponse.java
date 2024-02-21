package com.example.mspayment.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageablePaymentResponse {

    List<PaymentResponse> payments;
    Long totalElements;
    int totalPages;
    boolean hasNextPage;
}
