package com.example.mspayment.service.specification;

import com.example.mspayment.entity.Payment;
import com.example.mspayment.model.request.PaymentCriteria;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class PaymentSpecification implements Specification<Payment> {

    private final PaymentCriteria paymentCriteria;

    private static final String AMOUNT = "amount";
    private static final String DESCRIPTION = "description";

    @Override
    public Predicate toPredicate(Root<Payment> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

        List<Predicate> predicates = new ArrayList<>();
        if (paymentCriteria != null) {
            if (paymentCriteria.getAmountFrom() != null) {
                predicates.add(
                        criteriaBuilder.greaterThanOrEqualTo(root.get(AMOUNT), paymentCriteria.getAmountFrom()));
            }

            if (paymentCriteria.getAmountTo() != null) {
                predicates.add(
                        criteriaBuilder.lessThanOrEqualTo(root.get(AMOUNT), paymentCriteria.getAmountTo()));
            }

            if (StringUtils.hasText(paymentCriteria.getDescription())) {
                predicates.add(
                        criteriaBuilder.like(root.get(DESCRIPTION), "%" + paymentCriteria.getDescription() + "%"));
            }
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
