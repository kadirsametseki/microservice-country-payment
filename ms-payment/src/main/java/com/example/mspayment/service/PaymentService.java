package com.example.mspayment.service;

import com.example.mspayment.model.request.PaymentCriteria;
import com.example.mspayment.model.request.PaymentRequest;
import com.example.mspayment.model.response.PageablePaymentResponse;
import com.example.mspayment.model.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    void savePayment(PaymentRequest request);
    PaymentResponse getPaymentById(Long id);
    PageablePaymentResponse getAllPayments(int page, int count, PaymentCriteria paymentCriteria);
    void deletePaymentById(Long id);
    void updatePayment(Long id, PaymentRequest request);
}
