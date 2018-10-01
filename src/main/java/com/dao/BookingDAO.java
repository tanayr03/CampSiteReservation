package com.dao;


import com.domain.Booking;
import com.domain.BookingAvailability;

import java.awt.print.Book;
import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public interface BookingDAO {
	int save(Booking booking);
	List<Booking> getList(int id);
    List<Booking> getListFromTill(LocalDate from, LocalDate till,int bId);
    public Booking saveUpdate(Booking booking);
}
