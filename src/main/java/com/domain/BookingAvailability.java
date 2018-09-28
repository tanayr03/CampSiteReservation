package com.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;

public class BookingAvailability {

    private LocalDate date;
    private String status;

    public BookingAvailability(LocalDate date){
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
