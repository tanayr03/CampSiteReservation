package com.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Booking")
public class Booking {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private String email;
	private String fullName;
	private LocalDate arrival;
	private LocalDate departure;
	private String status;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public LocalDate getArrival() {
		return arrival;
	}

	public void setArrival(LocalDate arrival) {
		this.arrival = arrival;
	}

	public LocalDate getDeparture() {
		return departure;
	}

	public void setDeparture(LocalDate departure) {
		this.departure = departure;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString(){
		return "id="+id+", email="+email+", fullName="+fullName + ", arrival="+arrival + ", departure="+departure + ", status="+status ;
	}
}
