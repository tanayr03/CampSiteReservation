package com.service;


import com.domain.Booking;
import com.domain.BookingAvailability;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {
	int bookCampsite(Booking booking) throws Exception;
	List<BookingAvailability> getAvailability(LocalDate from,LocalDate till);
    Booking getBookingById(int id) throws Exception;
    Booking cancelBooking(int id) throws Exception;
    Booking updateBooking(Booking booking) throws Exception;

}
