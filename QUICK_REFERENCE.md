# Exception Handling Quick Reference Card

## 📋 Quick Facts

| Item | Details |
|------|---------|
| **Package** | `org.example.travel360.exception` |
| **Handler** | `GlobalExceptionHandler.java` with `@ControllerAdvice` |
| **Response Type** | `ErrorResponseDTO` (JSON) |
| **Logger** | Java's `java.util.logging.Logger` |
| **Scope** | Application-wide, all endpoints |

---

## 🎯 Exception Classes at a Glance

```
Exception Hierarchy:
├── BookingNotFoundException (404)
├── ReservationNotFoundException (404)
├── ItineraryNotFoundException (404)
├── InvalidBookingStatusException (400)
└── InvalidUserIdException (400)
```

---

## 🔧 How to Use

### Throw Exception (Service Layer)
```java
throw new BookingNotFoundException("Booking not found for User ID: " + userId);
```

### Handle Automatically
No try-catch needed! GlobalExceptionHandler catches it automatically.

### Get Timestamped Error JSON
```json
{
  "timestamp": "2024-05-12T10:30:45",
  "status": 404,
  "error": "BOOKING_NOT_FOUND",
  "message": "Booking not found for User ID: 999",
  "path": "/api/itinerary/generate/999"
}
```

---

## 📊 Status Code Mapping

```
400 Bad Request
├── INVALID_USER_ID
└── INVALID_BOOKING_STATUS

404 Not Found
├── BOOKING_NOT_FOUND
├── RESERVATION_NOT_FOUND
└── ITINERARY_NOT_FOUND

500 Internal Server Error
└── Any uncaught exception
```

---

## ✅ Validation Checks

```
User ID Validation:
└── if (userId == null || userId <= 0)
    └── throw InvalidUserIdException

Booking Existence:
└── if (bookingOptional.isEmpty())
    └── throw BookingNotFoundException

Booking Status:
└── if (!"CONFIRMED".equalsIgnoreCase(status))
    └── throw InvalidBookingStatusException

Reservation Existence:
└── if (reservationOptional.isEmpty())
    └── throw ReservationNotFoundException

Itinerary Existence:
└── if (itineraries.isEmpty())
    └── throw ItineraryNotFoundException
```

---

## 🌐 API Endpoints & Error Scenarios

```
POST /api/itinerary/generate/{userId}
├── 201 Created ✅ (Booking confirmed, reservation exists)
├── 400 INVALID_USER_ID ❌ (userId ≤ 0)
├── 400 INVALID_BOOKING_STATUS ❌ (Status not CONFIRMED)
├── 404 BOOKING_NOT_FOUND ❌ (No booking)
└── 404 RESERVATION_NOT_FOUND ❌ (No reservation)

GET /api/itinerary/{userId}
├── 200 OK ✅ (Itinerary found)
├── 400 INVALID_USER_ID ❌ (userId ≤ 0)
└── 404 ITINERARY_NOT_FOUND ❌ (No itinerary)

GET /api/itinerary/health
└── 200 OK ✅ ("Itinerary API is running!")
```

---

## 🔍 Error Response Structure

```
ErrorResponseDTO {
  LocalDateTime timestamp;     // When error occurred
  int status;                  // HTTP status code
  String error;                // Error code/type
  String message;              // Human-readable message
  String path;                 // API endpoint path
}
```

---

## 💡 Common Scenarios

### Scenario 1: Invalid Input
```
User tries: GET /api/itinerary/0
Response: 400 Bad Request → INVALID_USER_ID
```

### Scenario 2: Resource Not Found
```
User tries: GET /api/itinerary/999
Response: 404 Not Found → ITINERARY_NOT_FOUND
```

### Scenario 3: Business Rule Violation
```
User tries: POST /api/itinerary/generate/5 (booking status = PENDING)
Response: 400 Bad Request → INVALID_BOOKING_STATUS
```

### Scenario 4: Success
```
User tries: POST /api/itinerary/generate/1 (valid confirmed booking)
Response: 201 Created → ItineraryDTO object
```

---

## 🚀 Best Practices

| ✅ DO | ❌ DON'T |
|-----|---------|
| Throw exceptions for errors | Return null for errors |
| Use custom exceptions | Use generic Exception |
| Log errors with SEVERE level | Use println() |
| Return proper HTTP status | Always return 200 OK |
| Validate input early | Validate late |
| Use centralized handler | Try-catch in every method |
| Include error timestamp | No timestamp |
| Provide error codes | Vague error messages |

---

## 📧 Error Handling Flow

```
┌──────────────────┐
│   API Request    │
└────────┬─────────┘
         │
    ┌────▼────┐
    │Validate  │
    │Input     │
    └────┬────┘
         │
    ┌────▼──────────┐
    │Exception       │
    │Thrown?         │
    └────┬───┬──────┘
         │   │
        No  Yes
         │   │
         │   └─────────────────────┐
         │                         │
    ┌────▼────┐      ┌────────────▼──────┐
    │Process   │      │GlobalException     │
    │Success   │      │Handler Catches     │
    └────┬────┘      └────────────┬───────┘
         │                        │
         │            ┌───────────▼────────────┐
         │            │Create ErrorResponseDTO │
         │            └────────────┬───────────┘
         │                         │
    ┌────▼─────────────────────────▼──────┐
    │              Return JSON Response    │
    │   (with timestamp, status, error)    │
    └──────────────────────────────────────┘
```

---

## 🔧 Logging Patterns

### In Service Layer
```java
// Error logging
logger.severe("Booking not found for User ID: " + userId);

// Info logging
logger.info("Itinerary generated successfully");
```

### In Controller Layer
```java
// Request logging
logger.info("Received request to generate itinerary for userId: " + userId);

// Success logging
logger.info("Itinerary generated successfully for userId: " + userId);
```

---

## 📁 Files to Review

```
Learn Exception Handling:
├── EXCEPTION_HANDLING_GUIDE.md ← Detailed guide
├── IMPLEMENTATION_SUMMARY.md ← What was added
└── API_TESTING_GUIDE.md ← How to test

Source Code:
├── exception/
│   ├── *Exception.java files (5 files)
│   └── GlobalExceptionHandler.java
├── dto/
│   └── ErrorResponseDTO.java
├── service/
│   └── ItineraryService.java (updated)
└── controller/
    └── ItineraryRestController.java (updated)
```

---

## 🧪 Quick Test Commands

```bash
# Health check
curl -X GET http://localhost:8080/api/itinerary/health

# Invalid ID
curl -X POST http://localhost:8080/api/itinerary/generate/0

# Non-existent booking
curl -X POST http://localhost:8080/api/itinerary/generate/999

# Valid request
curl -X POST http://localhost:8080/api/itinerary/generate/1

# Get itinerary
curl -X GET http://localhost:8080/api/itinerary/1
```

---

## 📝 Exception Codes Reference

| Code | HTTP | Meaning |
|------|------|---------|
| INVALID_USER_ID | 400 | User ID validation failed |
| INVALID_BOOKING_STATUS | 400 | Booking not confirmed |
| BOOKING_NOT_FOUND | 404 | Missing booking record |
| RESERVATION_NOT_FOUND | 404 | Missing reservation |
| ITINERARY_NOT_FOUND | 404 | Missing itinerary |
| INTERNAL_SERVER_ERROR | 500 | Unexpected error |

---

## 🎓 Key Takeaways

1. **No More Null Returns** - Use exceptions instead
2. **Centralized Handling** - GlobalExceptionHandler manages all errors
3. **Standardized Responses** - Every error is consistent JSON
4. **Proper HTTP Status** - Correct status code for each error type
5. **Request Tracing** - Path field helps identify problem endpoint
6. **Better Logging** - SEVERE level logs track all errors
7. **Easy to Extend** - Adding new exceptions is straightforward
8. **Production Ready** - Follows Spring Boot best practices

---

**Print this card and keep it handy! 📌**

