package com.example.mspayment.controller;

import com.example.mspayment.model.request.PaymentCriteria;
import com.example.mspayment.model.request.PaymentRequest;
import com.example.mspayment.model.response.PageablePaymentResponse;
import com.example.mspayment.model.response.PaymentResponse;
import com.example.mspayment.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PaymentController.class)
public class PaymentControllerTest {

    private static final String PAYMENT_PATH = "/v1/payments";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    void savePayment_Success() throws Exception {
        //Arrange
        var request = new PaymentRequest();
        request.setDescription("description");
        request.setCurrency("uk");
        request.setAmount(BigDecimal.TEN);

        doNothing().when(paymentService).savePayment(request);

        //Act
        mockMvc.perform(post(PAYMENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        //Assert
        verify(paymentService, times(1)).savePayment(request);
    }

    @Test
    void savePayment_WhenAmountSizeExceed_BadRequest() throws Exception {
        //Arrange
        var request = new PaymentRequest();
        request.setDescription("description");
        request.setCurrency("uk");
        request.setAmount(BigDecimal.valueOf(5000));

        doNothing().when(paymentService).savePayment(request);

        //Act
        mockMvc.perform(post(PAYMENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        //Assert
        verifyNoInteractions(paymentService);
    }

    @Test
    void savePayment_WhenDescriptionIsBlank_BadRequest() throws Exception {
        //Arrange
        var request = new PaymentRequest();
        request.setDescription("");
        request.setCurrency("uk");
        request.setAmount(BigDecimal.valueOf(1000));

        doNothing().when(paymentService).savePayment(request);

        //Act
        mockMvc.perform(post(PAYMENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        //Assert
        verifyNoInteractions(paymentService);
    }

    @Test
    void savePayment_WhenCurrencyIsBlank_BadRequest() throws Exception {
        //Arrange
        var request = new PaymentRequest();
        request.setDescription("description");
        request.setCurrency("");
        request.setAmount(BigDecimal.valueOf(1000));

        doNothing().when(paymentService).savePayment(request);

        //Act
        mockMvc.perform(post(PAYMENT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        //Assert
        verifyNoInteractions(paymentService);
    }

    @Test
    void getAllPayments_Success() throws Exception {
        //Arrange
        var response = new PageablePaymentResponse();
        response.setTotalElements(5L);

        when(paymentService.getAllPayments(1, 5, new PaymentCriteria())).thenReturn(response);

        //Act
        mockMvc.perform(MockMvcRequestBuilders.get(PAYMENT_PATH)
                        .param("page", "1")
                        .param("count", "5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements", Matchers.is(5)))
                .andExpect(MockMvcResultMatchers.status().isOk());

        //Assert
        verify(paymentService, times(1)).getAllPayments(1, 5, new PaymentCriteria());
    }

    @Test
    void getPaymentById_Success() throws Exception {
        //Arrange
        var ID = 1L;

        var response = new PaymentResponse();
        response.setDescription("description");

        when(paymentService.getPaymentById(ID)).thenReturn(response);

        //Act
        mockMvc.perform(get(PAYMENT_PATH + "/{id}", ID))
                .andExpect(jsonPath("$.description", Matchers.is("description")))
                .andExpect(status().isOk());

        //Assert
        verify(paymentService, times(1)).getPaymentById(ID);
    }

    @Test
    void updatePayment_Success() throws Exception {
        //Arrange
        var ID = 1L;

        var request = new PaymentRequest();
        request.setDescription("description");

        doNothing().when(paymentService).updatePayment(ID, request);

        //Act
        mockMvc.perform(MockMvcRequestBuilders.put(PAYMENT_PATH + "/edit/{id}", ID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        //Assert
        verify(paymentService, times(1)).updatePayment(ID, request);
    }

    @Test
    void deletePayment_Success() throws Exception {
        //Arrange
        var ID = 1L;

        doNothing().when(paymentService).deletePaymentById(ID);

        //Act
        mockMvc.perform(MockMvcRequestBuilders.delete(PAYMENT_PATH + "/delete/{id}", ID))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        //Assert
        verify(paymentService, times(1)).deletePaymentById(ID);
    }
}
