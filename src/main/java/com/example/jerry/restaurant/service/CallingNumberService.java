package com.example.jerry.restaurant.service;

import java.util.List;

import com.example.jerry.restaurant.pojo.CallingNumber;

public interface CallingNumberService {

    CallingNumber getResult(int peoplenumber);

    List<CallingNumber> getResultList();

}
