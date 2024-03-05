package com.example.mspayment.mapper;

import com.example.mspayment.entity.Payment;
import com.example.mspayment.model.request.PaymentRequest;
import com.example.mspayment.model.response.PaymentResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class PaymentMapperTest {

    @Test
    void toEntityTest() {

        //Arrange
        var request = new PaymentRequest();
        request.setAmount(BigDecimal.ONE);
        request.setDescription("description");

        var expected = new Payment();
        expected.setAmount(BigDecimal.ONE);
        expected.setDescription("description");

        //Act(Actual)
        var actual = PaymentMapper.mapRequestToEntity(request);

        //Assert
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @Test
    void ToResponseTest() {

        //Arrange
        var request = new Payment();
        request.setAmount(BigDecimal.ONE);
        request.setDescription("description");

        var expected = new PaymentResponse();
        expected.setAmount(BigDecimal.ONE);
        expected.setDescription("description");

        //Act(Actual)
        var actual = PaymentMapper.mapEntityToResponse(request);

        //Assert
        Assertions.assertThat(actual.getAmount()).isEqualTo(expected.getAmount());
        Assertions.assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
    }

}
