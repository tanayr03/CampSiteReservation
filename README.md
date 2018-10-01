# CampSiteReservation


                        			Campsite Reservation

Link for API documentation through swaggerUI - 

http://campsite-reservapp.r6kpydcat4.us-east-2.elasticbeanstalk.com:8080/swagger-ui.html

1 – getAvailability API 

Description: Gets the availability of the campsite for dates passed with a default of 30 days from current day.

API - http://campsite-reservapp.r6kpydcat4.us-east-2.elasticbeanstalk.com:8080/getAvailability

parameters -
from -> "yyyy-mm-dd"
till -> "yyyy-mm-dd"

Sample Response Screenshot - 

Validations -
1. from and till date should have a yyyy-mm-dd format.
2. from and till if not entered returns availability for 30 days.

2 – POST API 

Description: Makes a post call to reserve a booking. Gives error if another booking is already present for the entered dates.

API – 

http://campsite-reservapp.r6kpydcat4.us-east-2.elasticbeanstalk.com:8080/reserveCampsite


SAMPLE REQUEST BODY – 

{
  "arrival": "2018-10-10",
  "departure": "2018-10-10",
  "email": "tanayr03@gmail.com",
  "fullName": "Tanay Rashinkar"
} 

Validations – 
1.	Departure and arrival date formats are yyyy-mm-dd.
2.	Arrival, departure, email and fullName fields are not null.
3.	Campsite arrival date should not be in the past and should be one day ahead of the current date.
4.	Both arrival and departure dates are included in the reservation and can have a max difference of 3 which is the maximum limit for a single booking.
5.	Checks if a booking is not already present for the given dates.
Response body – Booking Successful with booking ID.
3 – PUT API 

Description: This API is used for updating the reservation details like the email, firstname, arrival and departure dates. The booking will be validated by calling a getAvailability function for the specified dates and then updated.


API - http://localhost:8080/bookings/1

{
  "email": "t@g.com",
  "fullName": "Tanay Rash",
  "arrival": "2018-10-05",
  "departure": "2018-10-05"
}


Validations – 

1.	Email and fullname fields can be updated.
2.	Can update arrival and departure date according to the availability.
3.	Can extend/shorten his stay by updating departure/arrival dates.

4 – Get Booking API to get the reservation – 

Description: This API is used for getting the reservation by passing the booking id.


API - http://campsite-reservapp.r6kpydcat4.us-east-2.elasticbeanstalk.com:8080/bookings/4

Parameters – booking Id

Response Body – 

{
  "id": 4,
  "email": "tanayr03@gmail.com",
  "fullName": "Tanay Rashinkar",
  "arrival": "2018-10-09",
  "departure": "2018-10-09",
  "status": "confirmed"
}

Validations – 

In case booking is not found – 

Response Body –

 {
  "status": "BAD_REQUEST",
  "message": "Booking not found"
}

5 – DELETE API to delete the reservation – 

Description: This API is used for deleting the reservation by passing the booking ID. It changes the status of the booking from confirmed to cancelled.

API - http://campsite-reservapp.r6kpydcat4.us-east-2.elasticbeanstalk.com:8080/bookings/4

Parameters – booking ID to delete the booking.



