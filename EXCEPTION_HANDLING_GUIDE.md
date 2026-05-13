# Exception Handling Implementation Guide

## Overview
This document explains the comprehensive exception handling system implemented in the Travel360 project.

## 📁 File Structure

### 1. **Custom Exception Classes** (package: `org.example.travel360.exception`)

#### `BookingNotFoundException.java`
- Thrown when a booking record is not found in the database
- HTTP Status: 404 Not Found
- Error Code: `BOOKING_NOT_FOUND`

#### `ReservationNotFoundException.java`
- Thrown when a reservation record is not found in the database
- HTTP Status: 404 Not Found
- Error Code: `RESERVATION_NOT_FOUND`

#### `ItineraryNotFoundException.java`
- Thrown when an itinerary record is not found in the database
- HTTP Status: 404 Not Found
- Error Code: `ITINERARY_NOT_FOUND`

#### `InvalidBookingStatusException.java`
- Thrown when booking status is not "CONFIRMED"
- HTTP Status: 400 Bad Request
- Error Code: `INVALID_BOOKING_STATUS`

#### `InvalidUserIdException.java`
- Thrown when user ID is null or invalid (≤ 0)
- HTTP Status: 400 Bad Request
- Error Code: `INVALID_USER_ID`

### 2. **Global Exception Handler** (`GlobalExceptionHandler.java`)
- Centralized exception handling using `@ControllerAdvice` annotation
- Handles all custom exceptions and generic exceptions
- Returns standardized error responses

### 3. **Error Response DTO** (`ErrorResponseDTO.java`)
- Standardized JSON response structure for all errors
- Contains:
  - `timestamp`: When the error occurred
  - `status`: HTTP status code
  - `error`: Error type/code
  - `message`: Detailed error message
  - `path`: API endpoint that caused the error

## 🔄 Exception Flow

```
API Request
    ↓
Controller validates input
    ↓
Service throws custom exception (if validation fails)
    ↓
GlobalExceptionHandler catches exception
    ↓
Creates ErrorResponseDTO
    ↓
Returns JSON error response with appropriate HTTP status
```

## 📝 Service Layer Changes

### `ItineraryService.java`
The service now includes:

1. **Input Validation**
   - Validate `userId` is not null and > 0
   - Throws `InvalidUserIdException` if validation fails

2. **Exception Throwing**
   - `generateItinerary()`: Validates booking exists, status is CONFIRMED, and reservation exists
   - `getItineraryByUserId()`: Validates itinerary exists for the user

3. **Logging**
   - Uses Java's standard `Logger` class
   - Logs errors at `SEVERE` level when exceptions occur
   - Logs info messages during successful operations

### `ItineraryRestController.java`
The controller has been simplified:

1. **Removed Try-Catch Blocks**
   - No longer handles exceptions at controller level
   - Exceptions are handled by GlobalExceptionHandler

2. **Cleaner Code**
   - Focuses on request mapping and routing
   - Delegates error handling to the global handler

3. **Logging**
   - Logs incoming requests
   - Logs successful execution

## 📊 HTTP Status Codes Used

| Status | Exception | When Thrown |
|--------|-----------|------------|
| 400 Bad Request | InvalidUserIdException | User ID is null or ≤ 0 |
| 400 Bad Request | InvalidBookingStatusException | Booking status is not CONFIRMED |
| 404 Not Found | BookingNotFoundException | Booking not found for user |
| 404 Not Found | ReservationNotFoundException | Reservation not found for booking |
| 404 Not Found | ItineraryNotFoundException | Itinerary not found for user |
| 500 Internal Server Error | Generic Exception | Any unexpected error |

## 💡 Example Error Responses

### Example 1: Invalid User ID
```json
{
  "timestamp": "2024-05-12T10:30:45.123456",
  "status": 400,
  "error": "INVALID_USER_ID",
  "message": "User ID must be a positive number",
  "path": "/api/itinerary/generate/-1"
}
```

### Example 2: Booking Not Found
```json
{
  "timestamp": "2024-05-12T10:31:20.654321",
  "status": 404,
  "error": "BOOKING_NOT_FOUND",
  "message": "Booking not found for User ID: 999",
  "path": "/api/itinerary/generate/999"
}
```

### Example 3: Invalid Booking Status
```json
{
  "timestamp": "2024-05-12T10:32:15.789012",
  "status": 400,
  "error": "INVALID_BOOKING_STATUS",
  "message": "Booking is not CONFIRMED. Current status: PENDING",
  "path": "/api/itinerary/generate/5"
}
```

## 🚀 Usage Instructions

### 1. **Throwing Exceptions in Service/Repository**
```java
if (bookingOptional.isEmpty()) {
    throw new BookingNotFoundException("Booking not found for User ID: " + userId);
}
```

### 2. **Exception is Automatically Handled**
No need to try-catch in the controller. The GlobalExceptionHandler will:
- Catch the exception
- Create an ErrorResponseDTO
- Return appropriate HTTP status
- Return JSON response

### 3. **Adding New Exceptions**
To add a new custom exception:

1. Create a new exception class extending `RuntimeException`
2. Add `@ExceptionHandler` method in `GlobalExceptionHandler`
3. Return appropriate HTTP status and error code

Example:
```java
@ExceptionHandler(YourCustomException.class)
public ResponseEntity<?> handleYourCustomException(YourCustomException ex, WebRequest request) {
    ErrorResponseDTO errorResponse = new ErrorResponseDTO(
        HttpStatus.BAD_REQUEST.value(),
        "YOUR_ERROR_CODE",
        ex.getMessage(),
        request.getDescription(false).replace("uri=", "")
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
}
```

## 📋 Testing Scenarios

### Test 1: Invalid User ID
```
GET /api/itinerary/0  
Expected Response: 400 Bad Request with INVALID_USER_ID
```

### Test 2: Non-existent User
```
GET /api/itinerary/999  
Expected Response: 404 Not Found with ITINERARY_NOT_FOUND
```

### Test 3: Generate Itinerary Without Confirmed Booking
```
POST /api/itinerary/generate/3  
(Booking exists but status is not CONFIRMED)
Expected Response: 400 Bad Request with INVALID_BOOKING_STATUS
```

## 🔧 Logging Details

The application uses Java's built-in `java.util.logging.Logger` class:

- **Logger Instance**: `private static final Logger logger = Logger.getLogger(ClassName.class.getName());`
- **Error Level**: `logger.severe(message)` - for errors
- **Info Level**: `logger.info(message)` - for informational messages

All errors that throw exceptions are logged at SEVERE level for easy debugging.

## ⚠️ Important Notes

1. **No Null Returns**: The service no longer returns `null` for error cases. It throws exceptions instead.
2. **Consistent JSON Responses**: All error responses follow the same structure.
3. **Automatic HTTP Status**: GlobalExceptionHandler automatically sets the appropriate HTTP status code.
4. **Request Path Tracking**: Each error response includes the API path that caused it for debugging.

## 🔍 Future Enhancements

Possible improvements to the exception handling:
1. Add custom error codes/reference numbers
2. Add stacktrace in debug mode
3. Add retry-after headers for rate limiting
4. Add warning level logging
5. Add detailed validation error messages

