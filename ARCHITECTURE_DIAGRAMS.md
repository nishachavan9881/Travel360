# Exception Handling Architecture Diagram

## System Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          Travel360 Application                               │
│                                                                               │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │                    HTTP Client (Postman/Browser)                      │   │
│  └────────────────────────┬─────────────────────────────────────────────┘   │
│                          ▲                          │                         │
│                          │                          ▼                         │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │                     Spring Boot Web Layer                             │   │
│  │                                                                        │   │
│  │  ┌─────────────────────────────────────────────────────────────┐    │   │
│  │  │  ItineraryRestController (@RestController)                  │    │   │
│  │  │  ┌───────────────────────────────────────────────────────┐  │    │   │
│  │  │  │ POST /generate/{userId}  ──▶ Service Layer            │  │    │   │
│  │  │  │ GET /{userId}            ──▶ Service Layer            │  │    │   │
│  │  │  │ GET /health              ──▶ Returns health status     │  │    │   │
│  │  │  │                                                        │  │    │   │
│  │  │  │ Note: No try-catch blocks!                            │  │    │   │
│  │  │  │ Exceptions are handled globally                       │  │    │   │
│  │  │  └───────────────────────────────────────────────────────┘  │    │   │
│  │  └─────────────────────────────────────────────────────────────┘    │   │
│  │                                                                        │   │
│  │  ┌─────────────────────────────────────────────────────────────┐    │   │
│  │  │  Business Logic Layer                                        │    │   │
│  │  │  ┌───────────────────────────────────────────────────────┐  │    │   │
│  │  │  │  ItineraryService (@Service)                          │  │    │   │
│  │  │  │                                                        │  │    │   │
│  │  │  │  ✅ Validates userId                                  │  │    │   │
│  │  │  │  ✅ Checks booking exists                             │  │    │   │
│  │  │  │  ✅ Validates booking status = CONFIRMED              │  │    │   │
│  │  │  │  ✅ Checks reservation exists                         │  │    │   │
│  │  │  │                                                        │  │    │   │
│  │  │  │  ❌ Throws InvalidUserIdException                     │  │    │   │
│  │  │  │  ❌ Throws BookingNotFoundException                   │  │    │   │
│  │  │  │  ❌ Throws InvalidBookingStatusException              │  │    │   │
│  │  │  │  ❌ Throws ReservationNotFoundException               │  │    │   │
│  │  │  │  ❌ Throws ItineraryNotFoundException                 │  │    │   │
│  │  │  │                                                        │  │    │   │
│  │  │  │  Logging: logger.info() and logger.severe()           │  │    │   │
│  │  │  └───────────────────────────────────────────────────────┘  │    │   │
│  │  └─────────────────────────────────────────────────────────────┘    │   │
│  │                                                                        │   │
│  │  ┌─────────────────────────────────────────────────────────────┐    │   │
│  │  │  Data Access Layer                                           │    │   │
│  │  │  ┌───────────────────────────────────────────────────────┐  │    │   │
│  │  │  │  BookingRepository (interface)                        │  │    │   │
│  │  │  │  ReservationRepository (interface)                    │  │    │   │
│  │  │  │  ItineraryRepository (interface)                      │  │    │   │
│  │  │  │  EntityDTOMapper (interface)                          │  │    │   │
│  │  │  └───────────────────────────────────────────────────────┘  │    │   │
│  │  └─────────────────────────────────────────────────────────────┘    │   │
│  └──────────────────────────────────────────────────────────────────────┘   │
│                                                                               │
│  ┌──────────────────────────────────────────────────────────────────────┐   │
│  │             Global Exception Handler Layer (@ControllerAdvice)        │   │
│  │                                                                        │   │
│  │  ┌─────────────────────────────────────────────────────────────┐    │   │
│  │  │  GlobalExceptionHandler                                      │    │   │
│  │  │                                                              │    │   │
│  │  │  Exception Caught? ──────────────────┐                      │    │   │
│  │  │                                      │                      │    │   │
│  │  │  ├──▶ BookingNotFoundException       │                      │    │   │
│  │  │  │    └──▶ 404 BOOKING_NOT_FOUND    │                      │    │   │
│  │  │  │                                  │                      │    │   │
│  │  │  ├──▶ ReservationNotFoundException   │                      │    │   │
│  │  │  │    └──▶ 404 RESERVATION_NOT_   │                      │    │   │
│  │  │  │         FOUND                    │                      │    │   │
│  │  │  │                                  │                      │    │   │
│  │  │  ├──▶ ItineraryNotFoundException     │                      │    │   │
│  │  │  │    └──▶ 404 ITINERARY_NOT_FOUND  │                      │    │   │
│  │  │  │                                  │                      │    │   │
│  │  │  ├──▶ InvalidBookingStatusExc.      │                      │    │   │
│  │  │  │    └──▶ 400 INVALID_BOOKING_     │                      │    │   │
│  │  │  │         STATUS                   │                      │    │   │
│  │  │  │                                  │                      │    │   │
│  │  │  ├──▶ InvalidUserIdException        │                      │    │   │
│  │  │  │    └──▶ 400 INVALID_USER_ID      │                      │    │   │
│  │  │  │                                  │                      │    │   │
│  │  │  └──▶ Generic Exception             │                      │    │   │
│  │  │       └──▶ 500 INTERNAL_SERVER_     │                      │    │   │
│  │  │            ERROR                    │                      │    │   │
│  │  │                                     ▼                      │    │   │
│  │  │  ┌─────────────────────────────────────────────────────┐  │    │   │
│  │  │  │ Create ErrorResponseDTO                             │  │    │   │
│  │  │  │ {                                                   │  │    │   │
│  │  │  │   "timestamp": "2024-05-12T10:30:45",              │  │    │   │
│  │  │  │   "status": 404,                                    │  │    │   │
│  │  │  │   "error": "BOOKING_NOT_FOUND",                    │  │    │   │
│  │  │  │   "message": "Booking not found...",               │  │    │   │
│  │  │  │   "path": "/api/itinerary/generate/999"            │  │    │   │
│  │  │  │ }                                                   │  │    │   │
│  │  │  └─────────────────────────────────────────────────────┘  │    │   │
│  │  │                                                              │    │   │
│  │  │  Return ResponseEntity with status and error JSON          │    │   │
│  │  │  └──▶ HTTP 404 + JSON body                                 │    │   │
│  │  │                                                              │    │   │
│  │  └─────────────────────────────────────────────────────────────┘    │   │
│  └──────────────────────────────────────────────────────────────────────┘   │
│                                                                               │
│                           Database Connection Layer                          │
│                           (Via JPA/Spring Data)                              │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      ▼
                            ┌──────────────────┐
                            │   Database       │
                            │  (MySQL)         │
                            └──────────────────┘
```

---

## Exception Handling Flow (Detailed)

```
START: HTTP Request
  │
  ▼
┌─────────────────────────────────────┐
│ ItineraryRestController             │
│ @PostMapping /generate/{userId}     │
└────────────┬────────────────────────┘
            │
            ▼
┌─────────────────────────────────────┐
│ Call itineraryService.generateItinerary(userId)
└────────────┬────────────────────────┘
            │
            ▼
┌─────────────────────────────────────┐
│ VALIDATION POINT 1                  │
│ Check: userId == null || userId <= 0│
├─────────────────────────────────────┤
│ Result: PASS ✓                      │
│ Result: FAIL ✗ ──┐                 │
└────────────┬────────────────────────┘
            │        │
     PASS   │        │ FAIL
            │        │
            ▼        ▼
          ...    throwInvalidUserIdException()
                      │
                      ▼
                  ┌─────────────────────────────┐
                  │ GlobalExceptionHandler      │
                  │ Catches Exception           │
                  └────────────┬────────────────┘
                             │
                             ▼
                  ┌─────────────────────────────┐
                  │ Create ErrorResponseDTO     │
                  │ status: 400                 │
                  │ error: "INVALID_USER_ID"    │
                  │ message: "User ID must...   │
                  │ timestamp: <current time>   │
                  │ path: "/api/itinerary/..."  │
                  └────────────┬────────────────┘
                             │
                             ▼
                  ┌─────────────────────────────┐
                  │ Return ResponseEntity       │
                  │ HTTP 400 + JSON body        │
                  └────────────┬────────────────┘
                             │
                             ▼
                  Return to HTTP Client
                  (END - Error Path)
            │
            ▼
┌─────────────────────────────────────┐
│ VALIDATION POINT 2                  │
│ Check: bookingOptional.isEmpty()    │
├─────────────────────────────────────┤
│ Result: PASS ✓                      │
│ Result: FAIL ✗ ──┐                 │
└────────────┬────────────────────────┘
            │        │
     PASS   │        │ FAIL
            │        │
            ▼        ▼
          ...    throwBookingNotFoundException()
                      │
                      └──▶ (Same as above, 404 error)
            │
            ▼
┌─────────────────────────────────────┐
│ VALIDATION POINT 3                  │
│ Check: booking status = "CONFIRMED" │
├─────────────────────────────────────┤
│ Result: PASS ✓                      │
│ Result: FAIL ✗ ──┐                 │
└────────────┬────────────────────────┘
            │        │
     PASS   │        │ FAIL
            │        │
            ▼        ▼
          ...    throwInvalidBookingStatusExc()
                      │
                      └──▶ (Same as above, 400 error)
            │
            ▼
┌─────────────────────────────────────┐
│ VALIDATION POINT 4                  │
│ Check: reservationOptional.isEmp()  │
├─────────────────────────────────────┤
│ Result: PASS ✓                      │
│ Result: FAIL ✗ ──┐                 │
└────────────┬────────────────────────┘
            │        │
     PASS   │        │ FAIL
            │        │
            ▼        ▼
          ...    throwReservationNotFoundException()
                      │
                      └──▶ (Same as above, 404 error)
            │
            ▼
┌─────────────────────────────────────┐
│ All Validations PASSED ✓            │
│ Create/Update Itinerary             │
│ Save to Database                    │
│ Convert to DTO                      │
└────────────┬────────────────────────┘
            │
            ▼
┌─────────────────────────────────────┐
│ Return ItineraryDTO object          │
└────────────┬────────────────────────┘
            │
            ▼
┌─────────────────────────────────────┐
│ ResponseEntity.status(201)          │
│ .body(itineraryDTO)                 │
└────────────┬────────────────────────┘
            │
            ▼
Return to HTTP Client
HTTP 201 Created + ItineraryDTO JSON
(END - Success Path)
```

---

## Exception Class Hierarchy

```
java.lang.Throwable
│
├── java.lang.Exception
│   │
│   └── java.lang.RuntimeException
│       │
│       ├── org.example.travel360.exception.BookingNotFoundException
│       │   └── Thrown when booking not found
│       │       HTTP Status: 404
│       │
│       ├── org.example.travel360.exception.ReservationNotFoundException
│       │   └── Thrown when reservation not found
│       │       HTTP Status: 404
│       │
│       ├── org.example.travel360.exception.ItineraryNotFoundException
│       │   └── Thrown when itinerary not found
│       │       HTTP Status: 404
│       │
│       ├── org.example.travel360.exception.InvalidBookingStatusException
│       │   └── Thrown when booking not confirmed
│       │       HTTP Status: 400
│       │
│       └── org.example.travel360.exception.InvalidUserIdException
│           └── Thrown when userId is invalid
│               HTTP Status: 400
```

---

## Request-Response Cycle

### Success Scenario

```
┌─────────────┐
│   Client    │
│  (Postman)  │
└──────┬──────┘
       │
       │ POST /api/itinerary/generate/1
       │ (userId=1, valid booking, confirmed status, reservation exists)
       │
       ▼
┌──────────────────────────────────┐
│   ItineraryRestController        │
├──────────────────────────────────┤
│ generateItinerary(1)             │
└──────────┬───────────────────────┘
           │
           ▼
┌──────────────────────────────────┐
│   ItineraryService               │
├──────────────────────────────────┤
│ ✓ userId validation passed       │
│ ✓ Booking found                  │
│ ✓ Booking status = CONFIRMED     │
│ ✓ Reservation found              │
│ ✓ Itinerary created/updated      │
│ ✓ Converted to DTO               │
└──────────┬───────────────────────┘
           │
           ▼
┌──────────────────────────────────┐
│   Return ItineraryDTO            │
├──────────────────────────────────┤
│ {                                │
│   "itineraryId": 1,              │
│   "userId": 1,                   │
│   "startDate": "2024-05-15",    │
│   "endDate": "2024-05-20",      │
│   "status": "CONFIRMED"          │
│ }                                │
└──────────┬───────────────────────┘
           │
           ▼ HTTP 201 Created
┌─────────┐
│ Client  │
│Receives │
│JSON     │
└─────────┘
```

### Error Scenario

```
┌─────────────┐
│   Client    │
│  (Postman)  │
└──────┬──────┘
       │
       │ POST /api/itinerary/generate/999
       │ (userId=999, booking doesn't exist)
       │
       ▼
┌──────────────────────────────────┐
│   ItineraryRestController        │
├──────────────────────────────────┤
│ generateItinerary(999)           │
└──────────┬───────────────────────┘
           │
           ▼
┌──────────────────────────────────┐
│   ItineraryService               │
├──────────────────────────────────┤
│ ✓ userId validation passed       │
│ ✗ Booking not found              │
│   throw BookingNotFoundException  │
└──────────┬───────────────────────┘
           │
           ▼ Exception thrown
           │ (NOT caught here)
           │
           ▼
┌──────────────────────────────────┐
│ GlobalExceptionHandler           │
├──────────────────────────────────┤
│ @ExceptionHandler(               │
│   BookingNotFoundException.class) │
├──────────────────────────────────┤
│ Create ErrorResponseDTO:         │
│ {                                │
│   "timestamp": "2024-05-12...",│
│   "status": 404,                 │
│   "error": "BOOKING_NOT_FOUND",  │
│   "message": "Booking not found..│
│   "path": "/api/itinerary/..."   │
│ }                                │
└──────────┬───────────────────────┘
           │
           ▼ HTTP 404 Not Found
┌─────────┐
│ Client  │
│Receives │
│404 with │
│Error    │
│JSON     │
└─────────┘
```

---

## Data Flow Diagram

```
                    USER INPUT
                        │
                        ▼
                ┌───────────────┐
                │  Validation   │
                │    Rules      │
                └───────┬───────┘
                        │
        ┌───────────────┼───────────────┐
        │               │               │
    PASS ✓          FAIL ✗ FAIL ✗  FAIL ✗
        │               │               │
        │        Exception1     Exception2
        │               │               │
        ▼               ▼               ▼
    Process      GlobalHandler   GlobalHandler
    Data         Catches Exception Catches Exception
        │               │               │
        ▼               ▼               ▼
    Database       Create Error   Create Error
    Query          Response JSON   Response JSON
        │               │               │
        ▼               ▼               ▼
    Return Data    Return 40X      Return 40X
    + 201/200       (with error)    (with error)
        │               │               │
        └───────────────┼───────────────┘
                        │
                        ▼
                    HTTP Response


                    (to Client)
```

---

## Component Interaction Matrix

```
┌──────────────────┬────────────────┬────────────────┬──────────────┐
│                  │  Controller    │  Service       │  Handler     │
├──────────────────┼────────────────┼────────────────┼──────────────┤
│ Responsibility   │ Route requests │ Validate &     │ Handle errors│
│                  │ Log activity   │ process logic  │ Format JSON  │
│                  │ Return response│ Throw errors   │ Set status   │
├──────────────────┼────────────────┼────────────────┼──────────────┤
│ Interacts With   │ Service        │ Repositories   │ Exceptions   │
│                  │ HTTP Layer     │ DTOs           │ ErrorDTO     │
├──────────────────┼────────────────┼────────────────┼──────────────┤
│ Error Handling   │ None (relies   │ Throws custom  │ Catches all  │
│                  │ on global)     │ exceptions     │ exceptions   │
├──────────────────┼────────────────┼────────────────┼──────────────┤
│ Logging          │ INFO level     │ SEVERE & INFO  │ SEVERE level │
│                  │ (requests)     │ (all errors)   │ (errors)     │
└──────────────────┴────────────────┴────────────────┴──────────────┘
```

---

This architecture ensures clean separation of concerns and maintainable code! 🎯

