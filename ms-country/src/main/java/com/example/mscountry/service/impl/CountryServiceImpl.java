package com.example.mscountry.service.impl;

import com.example.mscountry.model.CountryResponse;
import com.example.mscountry.service.CountryService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {
    @Override
    public List<CountryResponse> getAllAvailableCountries(String currency) {
        if (currency.equals("UK")) {
            return List.of(new CountryResponse("USA", BigDecimal.valueOf(1000)),
                    new CountryResponse("GER", BigDecimal.TEN));
        }

        return new ArrayList<>();
    }
}
