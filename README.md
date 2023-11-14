Parking API Documentation

Overview
The Parking API provides endpoints to manage parking lots, parking spots, and reservation-related operations.

Assumption
  1. Max parking slot is 100.
  2. Parking fee 2$ per hour.
  3. Used hashmap of storing and retriving instead of DB.
  4. During checkout calculate and return the fee.
  5. Used reentrantlock to checkInbySpot and checkInByReservation methods to control concurrency. but which will support single JVM only.

Base URL: /api/parking

Endpoints
1. Get All Parking Lots
      Request
        Method: GET
        Endpoint: /lots
        Response
        Status Code: 200 OK
        Body: List of ParkingLot objects
      Example
      curl -X GET http://localhost:8080/api/parking/lots
2. Get Parking Lot by ID
    Request
      Method: GET
      Endpoint: /lots/{id}
      Parameters
      id (path variable): Parking Lot ID
    Response
      Status Code: 200 OK if found, 404 Not Found if not found
      Body: ParkingLot object if found, error message if not found
    Example
    curl -X GET http://localhost:8080/api/parking/lots/1
4. Get All Parking Spots
    Request
      Method: GET
      Endpoint: /spots
      Response
      Status Code: 200 OK
      Body: List of ParkingSpot objects
    Example
      curl -X GET http://localhost:8080/api/parking/spots
5. Get Parking Spot by ID
    Request
      Method: GET
      Endpoint: /spots/{id}
      Parameters
      id (path variable): Parking Spot ID  
    Response
      Status Code: 200 OK if found, 404 Not Found if not found
      Body: ParkingSpot object if found, error message if not found  
    Example
      curl -X GET http://localhost:8080/api/parking/spots/1  
6. Get Available Parking Spots
    Request
      Method: GET
      Endpoint: /available/spots
      Response
      Status Code: 200 OK
      Body: List of available ParkingSpot objects
    Example
      curl -X GET http://localhost:8080/api/parking/available/spots
7. Reserve Parking Spot
    Request
      Method: POST
      Endpoint: /reserve
      Body: ReservationRequest JSON object
      Response
      Status Code: 200 OK
      Body: Success message or error message
    Example
      curl -X POST -H "Content-Type: application/json" -d '{"spotId": 1}' http://localhost:8080/api/parking/reserve
8. Check-In by Spot ID
    Request
      Method: POST
      Endpoint: /check-in-by-spot-id
      Body: CheckInRequest JSON object
      Response
      Status Code: 200 OK
    Body: Success message or error message
    Example
      curl -X POST -H "Content-Type: application/json" -d '{"id": 1}' http://localhost:8080/api/parking/check-in-by-spot-id
9. Check-In by Reservation ID
    Request
      Method: POST
      Endpoint: /check-in-by-reservation-id
      Body: CheckInRequest JSON object
    Response
      Status Code: 200 OK
    Body: Success message or error message
    Example
    curl -X POST -H "Content-Type: application/json" -d '{"id": 1}' http://localhost:8080/api/parking/check-in-by-reservation-id
10. Check-Out
    Request
      Method: POST
      Endpoint: /check-out
      Body: CheckOutRequest JSON object
    Response
      Status Code: 200 OK
    Body: Check-out details or error message
    Example
    curl -X POST -H "Content-Type: application/json" -d '{"spotId": 1, "reservationId": 2}' http://localhost:8080/api/parking/check-out
    
Error Handling
If an endpoint encounters an error, it will return an appropriate HTTP status code along with an error message in the response body.

Notes
Ensure proper authentication and authorization mechanisms are not in place.

Swagger Documention :

After starting the springboot application, then open http://localhost:8080/swagger-ui/index.html and enter in the text box /v2/api-docs
which will show you the api documentation.

