package com.example.hotcinemas_be.services;

import com.example.hotcinemas_be.dtos.booking.requests.BookingRequest;
import com.example.hotcinemas_be.dtos.booking.responses.BookingResponse;

public interface BookingService {
    public BookingResponse createBooking(BookingRequest bookingRequest);
}
