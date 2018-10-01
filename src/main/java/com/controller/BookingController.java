package com.controller;

import com.ApiError;
import com.domain.Booking;
import com.domain.BookingAvailability;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.service.BookingService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Api(value = "Bookings", description = "Manage bookings by using these APIs") // Swagger annotation
@RestController
public class BookingController {

    @Autowired
    BookingService bookingService;

    @ApiIgnore
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @CrossOrigin
    public String welcome() {
        return "Reserve Campsite!";
    }

    /**
     * This function creates a new booking object.
     *
     *
     * @throws Exception
     */
    @RequestMapping(value = "/reserveCampsite",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> reserveCampsite(@RequestBody Booking booking){
        try {
            int id = bookingService.bookCampsite(booking);

            return new ResponseEntity<String>(" {Booking Successful - Booking ID - : " + id + "}", HttpStatus.CREATED);
        } catch (Exception e) {
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,e.getMessage());
            return new ResponseEntity(apiError , HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getAvailability",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @CrossOrigin
    public ResponseEntity<?> checkAvailability(@RequestParam(value = "from",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate from, @RequestParam(value = "till",required=false) @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate till){
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
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,"Unable to getAvailability at this time");
            return new ResponseEntity(apiError , HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @CrossOrigin
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
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,e.getMessage());
            return new ResponseEntity(apiError , HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @CrossOrigin
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
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,e.getMessage());
            return new ResponseEntity(apiError , HttpStatus.BAD_REQUEST);
        }
    }

    @ResponseBody
    @CrossOrigin
    @RequestMapping(value = "/bookings/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Booking> updateBooking(@PathVariable("id") int id, @RequestBody Booking user) {
        try{
            Booking currentBooking = bookingService.getBookingById(id);
            if (currentBooking==null) {
                return new ResponseEntity<Booking>(HttpStatus.NOT_FOUND);
            }
            currentBooking.setEmail(user.getEmail());
            currentBooking.setFullName(user.getFullName());
            if(user.getDeparture()!=null) {
                currentBooking.setDeparture(user.getDeparture());
            }
            if(user.getArrival()!=null){
                currentBooking.setArrival(user.getArrival());
            }
            if(user.getId()!=id){
                throw new Exception("Booking ID cannot be modified!!");
            }

            if(!(user.getStatus() ==null || user.getStatus().isEmpty() || user.getStatus().equalsIgnoreCase(currentBooking.getStatus()))){
                throw new Exception("Cannot modify status");
            }

            bookingService.updateBooking(currentBooking);
            return new ResponseEntity<Booking>(currentBooking, HttpStatus.OK);
        }
        catch (Exception e){
            ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,e.getMessage());
            return new ResponseEntity(apiError , HttpStatus.BAD_REQUEST);
        }
    }
}
