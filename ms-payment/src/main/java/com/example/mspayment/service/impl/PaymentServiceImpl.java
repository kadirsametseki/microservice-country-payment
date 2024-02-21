package com.example.mspayment.service.impl;

import com.example.mspayment.client.CountryClient;
import com.example.mspayment.entity.Payment;
import com.example.mspayment.exception.NotFoundException;
import com.example.mspayment.mapper.PaymentMapper;
import com.example.mspayment.model.request.PaymentCriteria;
import com.example.mspayment.model.request.PaymentRequest;
import com.example.mspayment.model.response.PageablePaymentResponse;
import com.example.mspayment.model.response.PaymentResponse;
import com.example.mspayment.repository.PaymentRepository;
import com.example.mspayment.service.PaymentService;
import com.example.mspayment.service.specification.PaymentSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import static com.example.mspayment.mapper.PaymentMapper.mapEntityToResponse;
import static com.example.mspayment.mapper.PaymentMapper.mapRequestToEntity;
import static com.example.mspayment.model.constant.ExceptionConstants.COUNTRY_NOT_FOUND_CODE;
import static com.example.mspayment.model.constant.ExceptionConstants.COUNTRY_NOT_FOUND_MESSAGE;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final CountryClient countryClient;

    @Override
    public void savePayment(PaymentRequest request) {
        log.info("savePayment.started");
        countryClient.getAllAvailableCountries(request.getCurrency())
                .stream()
                .filter(country -> country.getRemainingLimit().compareTo(request.getAmount()) > 0)
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format(COUNTRY_NOT_FOUND_MESSAGE, request.getAmount(),
                        request.getCurrency()), COUNTRY_NOT_FOUND_CODE));

        paymentRepository.save(mapRequestToEntity(request));
        log.info("savePayment.success");
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {
        log.info("getPayment.start id: {}", id);
        Payment payment = fetchPaymentIfExistsById(id);
        log.info("getPayment.success id: {}", id);
        return mapEntityToResponse(payment);
    }

    @Override
    public PageablePaymentResponse getAllPayments(int page, int count, PaymentCriteria paymentCriteria) {
        log.info("getAllPayments.started");

        var pageable = PageRequest.of(page, count, Sort.by(DESC, "id"));

        var pageablePayments = paymentRepository.findAll(
                new PaymentSpecification(paymentCriteria), pageable);

        var payments = pageablePayments.getContent()
                .stream()
                .map(PaymentMapper::mapEntityToResponse).collect(Collectors.toList());

        log.info("getAllPayments.success");

        return PageablePaymentResponse.builder()
                .payments(payments)
                .hasNextPage(pageablePayments.hasNext())
                .totalElements(pageablePayments.getTotalElements())
                .totalPages(pageablePayments.getTotalPages())
                .build();
    }

    @Override
    public void deletePaymentById(Long id) {
        log.info("deletePayment.start id: {}", id);
        Payment payment = fetchPaymentIfExistsById(id);
        paymentRepository.deleteById(id);
        log.info("deletePayment.success id: {}", id);
    }

    @Override
    public void updatePayment(Long id, PaymentRequest request) {
        log.info("updatePayment.start id: {}", id);
        Payment payment = fetchPaymentIfExistsById(id);
        payment.setDescription(request.getDescription());
        payment.setAmount(request.getAmount());
        paymentRepository.save(payment);
        log.info("updatePayment.success id: {}", id);
    }


    private Payment fetchPaymentIfExistsById(Long id) {
        return paymentRepository.findById(id).orElseThrow(RuntimeException::new);
    }
}
