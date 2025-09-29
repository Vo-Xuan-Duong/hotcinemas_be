package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.dtos.requests.BookingRequest;
import com.example.hotcinemas_be.dtos.responses.BookingResponse;

public interface BookingService {
    public BookingResponse createBooking(BookingRequest bookingRequest);
}
