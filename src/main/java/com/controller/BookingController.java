package com.controller;

import com.domain.Booking;
import com.domain.BookingAvailability;
import com.service.BookingService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Api(value = "contacts", description = "contacts") // Swagger annotation
@RestController
public class BookingController {

    @Autowired
    BookingService bookingService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String helloUser() {
        return "Reserve Campsite!";
    }

    /**
     * This function creates a new booking object.
     *
     *
     * @throws Exception
     */
    @RequestMapping(value = "/reserveCampsite",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> reserveCampsite(@RequestBody Booking booking){
        try {
            int id = bookingService.bookCampsite(booking);
            return new ResponseEntity("Booking Successful - Booking ID - " + id, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity("Unable to Book at this time..." + e, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getAvailability",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> getDefaultAvailability(@RequestParam(value = "from",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate from, @RequestParam(value = "till",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate till){
        try {
            if(from == null){
                from = LocalDate.now();
            }

            if(till == null){
                till = from.plusDays(30);
            }
            List<BookingAvailability> availability = bookingService.getAvailability(from,till);
            return new ResponseEntity(availability, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
            return new ResponseEntity("Unable to Book At this time." + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/bookings/{id}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getBooking(@PathVariable  int id){
        try {
            Booking booking = bookingService.getBookingById(id);
            if(booking==null){
                throw new Exception("Cannot find the booking by this id");
            }
            return new ResponseEntity(booking , HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity( e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @RequestMapping(value = "/bookings/{id}",method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteBooking(@NotNull @PathVariable  int id){
        try {
            Booking bookingAfterCancellation = bookingService.cancelBooking(id);
            if(bookingAfterCancellation==null){
                throw new Exception("Cannot find the booking by this id");
            }
            return new ResponseEntity( bookingAfterCancellation , HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity( e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/bookings/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Booking> updateBooking(@PathVariable("id") int id, @RequestBody Booking user) {
        try{
            Booking currentBooking = bookingService.getBookingById(id);
            if (currentBooking==null) {
                System.out.println("User with id " + id + " not found");
                return new ResponseEntity<Booking>(HttpStatus.NOT_FOUND);
            }
            currentBooking.setEmail(user.getEmail());
            currentBooking.setFullName(user.getFullName());
            bookingService.updateBooking(currentBooking);
            return new ResponseEntity<Booking>(currentBooking, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity( e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
