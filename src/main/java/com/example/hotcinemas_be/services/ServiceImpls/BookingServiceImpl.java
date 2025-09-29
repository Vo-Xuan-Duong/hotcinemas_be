package com.example.hotcinemas_be.services.ServiceImpls;

import com.example.hotcinemas_be.dtos.requests.BookingRequest;
import com.example.hotcinemas_be.dtos.responses.BookingResponse;
import com.example.hotcinemas_be.repositorys.BookingRepository;
import com.example.hotcinemas_be.repositorys.SeatRepository;
import com.example.hotcinemas_be.repositorys.ShowtimeRepository;
import com.example.hotcinemas_be.services.BookingService;
import org.springframework.stereotype.Service;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;
    private final ShowtimeRepository showtimeRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              SeatRepository seatRepository,
                              ShowtimeRepository showtimeRepository) {
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
        this.showtimeRepository = showtimeRepository;
    }

    @Override
    public BookingResponse createBooking(BookingRequest bookingRequest) {
        return null;
    }
}
