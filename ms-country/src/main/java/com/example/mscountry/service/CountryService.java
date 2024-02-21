package com.example.mscountry.service;

import com.example.mscountry.model.CountryResponse;

import java.util.List;

public interface CountryService {

    List<CountryResponse> getAllAvailableCountries(String currency);
}
