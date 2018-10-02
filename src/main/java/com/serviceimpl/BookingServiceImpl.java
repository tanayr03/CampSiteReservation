package com.serviceimpl;

import com.dao.BookingDAO;
import com.domain.Booking;
import com.domain.BookingAvailability;
import com.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	BookingDAO bookingDAO;
    @Override
	public int bookCampsite(Booking booking) throws Exception {
	        validateBooking(booking,-1);
	        booking.setStatus("confirmed");
            return bookingDAO.save(booking);
	}

    @Override
    public Booking getBookingById(int id) throws Exception{
        List<Booking> list =  bookingDAO.getList(id);
        if(list.size()==0){
            throw new Exception("Booking not found");
        }
        return list.get(0);
    }

    @Override
    public Booking cancelBooking(int id) throws Exception{
        List<Booking> list =  bookingDAO.getList(id);
        if(list.size()==0){
            throw new Exception("Booking not found");
        }
        Booking bookingToCancel = list.get(0);
        bookingToCancel.setStatus("cancelled");
        bookingDAO.saveUpdate(bookingToCancel);
        return bookingToCancel;
    }

    @Override
    public Booking updateBooking(Booking booking) throws Exception{
        validateBooking(booking,booking.getId());
        bookingDAO.saveUpdate(booking);
        return booking;
    }

    @Override
    public List<BookingAvailability> getAvailability(LocalDate from, LocalDate till){
        LocalDate from3 = from.minusDays(3);
        LocalDate till3 = till.plusDays(3);
        List<BookingAvailability> bookings = new ArrayList<>();
        HashSet<Date> bookedDatesSet = getBookedDatesHashSet(from3,till3,-1);
        LocalDate x = from;

        while(!x.isEqual(till.plusDays(1))){
            BookingAvailability b = new BookingAvailability(x);
            b.setDate(x);
            if(!bookedDatesSet.contains(Date.valueOf(x))){
                b.setStatus("Available");
            }
            else{
                b.setStatus("Booked");
            }
            bookings.add(b);
            x = x.plusDays(1);
        }

        Collections.sort(bookings,(b1, b2) -> {
            if(b1.getDate().isAfter(b2.getDate())){
                return 1;
            }
            else if (b2.getDate().isAfter(b1.getDate())){
                return -1;
            }
            return 0;
        });
        return bookings ;
    }

    private boolean checkAvailability(LocalDate from, LocalDate till,int bId) throws Exception {
        boolean available = false;
        LocalDate from3 = from.minusDays(3);
        LocalDate till3 = till.plusDays(3);
        HashSet<Date> bookedDatesSet = getBookedDatesHashSet(from3,till3,bId);
        LocalDate x = from;
        while(!(x.isEqual(till.plusDays(1)))){
            if(bookedDatesSet.contains(Date.valueOf(x))){
                available = false;
                throw new Exception("Campsite is already reserved on " + Date.valueOf(x));
            }
            x = x.plusDays(1);
        }
        available = true;
        return available;
    }

	private HashSet<Date> getBookedDatesHashSet(LocalDate from3,LocalDate till3,int bId){
        List<Booking> bookings = bookingDAO.getListFromTill(from3,till3,bId);
        HashSet<Date> bookedDatesSet = new HashSet<>();
        for(Booking b:bookings){
            System.out.println(b);
            if(b.getStatus()!=null && b.getStatus().equalsIgnoreCase("confirmed")) {
                LocalDate localArrivalDate = b.getArrival();
                LocalDate localDepartureDate = b.getDeparture();
                LocalDate currentDate = localArrivalDate;

                while (!(currentDate.isEqual(localDepartureDate.plusDays(1)))) {
                    bookedDatesSet.add(Date.valueOf(currentDate));
                    currentDate = currentDate.plusDays(1);
                }
            }
        }
        return bookedDatesSet;
    }

    private void validateBooking(Booking booking,int bId) throws Exception{

	    if(booking == null){
            throw new Exception("Invalid details");
        }

        if(null==booking.getEmail() || !isValidEmailAddress(booking.getEmail())){
            throw new Exception("Please enter a valid Email");
        }

        if(null == booking.getFullName() || booking.getFullName().isEmpty()){
            throw new Exception("Full Name cannot be empty");
        }
        else if ( booking.getFullName().length() > 50){
            throw new Exception("Full Name cannot be more than 50 characters");
        }

        if(booking.getArrival() == null){
            throw new Exception("Please specify arrival date");
        }

        if(booking.getDeparture() == null){
            throw new Exception("Please specify departure date");
        }

        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalDate dateAfter30Days = LocalDate.now().plusDays(30);

        if(tomorrow.isAfter(booking.getArrival()) || tomorrow.isEqual(booking.getArrival())){
            throw new Exception("Please specify arrival date after " + tomorrow);
        }

        if(booking.getDeparture().isBefore(booking.getArrival())){
            throw new Exception("Departure date must be after Arrival date");
        }

        if(booking.getArrival().isAfter(dateAfter30Days)){
            throw new Exception("Please specify arrival date within " + dateAfter30Days);
        }

        int totaldays = 0;
        LocalDate currentDate = booking.getArrival();

        while(!(currentDate.isEqual(booking.getDeparture().plusDays(1)))){
            totaldays++;
            currentDate = currentDate.plusDays(1);
            if(totaldays>3){
                throw new Exception("You can book the campsite for a maximum of three days");
            }
        }
        checkAvailability(booking.getArrival(),booking.getDeparture(),bId);
    }

    private static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }

}
