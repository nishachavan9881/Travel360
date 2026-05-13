# 📋 ERROR RESPONSE FORMAT - 4 Field Format (NEW)

## ✅ New Error Response Format

Your error responses now use the **4-field format** you specified:

```json
{
    "error": "BOOKING_NOT_CONFIRMED",
    "message": "Booking is not CONFIRMED. Current status: PENDING",
    "status": 400,
    "timestamp": "2026-04-18T22:30:00"
}
```

---

## 📊 All Error Scenarios

### 1. Booking Not Found (404)
```json
{
    "error": "BOOKING_NOT_FOUND",
    "message": "Booking not found for User ID: 113",
    "status": 404,
    "timestamp": "2026-05-13T10:30:11"
}
```

### 2. Reservation Not Found (404)
```json
{
    "error": "RESERVATION_NOT_FOUND",
    "message": "Reservation not found for Booking ID: 1",
    "status": 404,
    "timestamp": "2026-05-13T10:30:11"
}
```

### 3. Itinerary Not Found (404)
```json
{
    "error": "ITINERARY_NOT_FOUND",
    "message": "Itinerary not found for User ID: 50",
    "status": 404,
    "timestamp": "2026-05-13T10:30:11"
}
```

### 4. Invalid Booking Status (400)
```json
{
    "error": "BOOKING_NOT_CONFIRMED",
    "message": "Booking is not CONFIRMED. Current status: PENDING",
    "status": 400,
    "timestamp": "2026-05-13T10:30:11"
}
```

### 5. Invalid User ID (400)
```json
{
    "error": "INVALID_USER_ID",
    "message": "User ID must be a positive number",
    "status": 400,
    "timestamp": "2026-05-13T10:30:11"
}
```

### 6. Server Error (500)
```json
{
    "error": "INTERNAL_SERVER_ERROR",
    "message": "An unexpected error occurred: [details]",
    "status": 500,
    "timestamp": "2026-05-13T10:30:11"
}
```

---

## 🔄 Field Explanation

| Field | Purpose | Example |
|-------|---------|---------|
| `error` | Error code identifier | `BOOKING_NOT_CONFIRMED` |
| `message` | Human-readable error description | `Booking is not CONFIRMED...` |
| `status` | HTTP status code (integer) | `400`, `404`, `500` |
| `timestamp` | When error occurred (ISO format) | `2026-05-13T10:30:11` |

---

## 🧪 Test in Postman

### Test 1: User Without Booking

**Request**:
```
POST http://localhost:8080/api/itinerary/generate/113
```

**Response** (404):
```json
{
    "error": "BOOKING_NOT_FOUND",
    "message": "Booking not found for User ID: 113",
    "status": 404,
    "timestamp": "2026-05-13T10:30:11"
}
```

---

### Test 2: User With PENDING Booking

**Request**:
```
POST http://localhost:8080/api/itinerary/generate/103
```

**Response** (400):
```json
{
    "error": "BOOKING_NOT_CONFIRMED",
    "message": "Booking is not CONFIRMED. Current status: PENDING",
    "status": 400,
    "timestamp": "2026-05-13T10:30:11"
}
```

---

### Test 3: Success (201)

**Request**:
```
POST http://localhost:8080/api/itinerary/generate/1
```

**Response** (201 Created):
```json
{
    "itineraryId": 1,
    "userId": 1,
    "bookingId": 1,
    "startDate": "2024-05-15",
    "endDate": "2024-05-20",
    "status": "CONFIRMED"
}
```

---

## 📝 Error Code Reference

```
Error Code                | Status | Meaning
─────────────────────────┼────────┼──────────────────────────
BOOKING_NOT_FOUND        | 404    | User has no booking
RESERVATION_NOT_FOUND    | 404    | Booking has no reservation
ITINERARY_NOT_FOUND      | 404    | Itinerary doesn't exist
BOOKING_NOT_CONFIRMED    | 400    | Booking is not CONFIRMED
INVALID_USER_ID          | 400    | User ID ≤ 0 or null
INTERNAL_SERVER_ERROR    | 500    | Unexpected error
```

---

## 🚀 Quick Commands to Deploy

### Step 1: Stop Current App
```
Press Ctrl + C
```

### Step 2: Clean & Compile
```bash
cd travel360
mvn clean compile
```

### Step 3: Start App
```bash
mvn spring-boot:run
```

### Step 4: Test in Postman
- Send POST to `/api/itinerary/generate/113`
- See 4-field error format

---

## ✅ Features

✅ **4 Essential Fields** - error, message, status, timestamp
✅ **Error Codes** - Clear identifier for each error type
✅ **Human-Readable Messages** - Users understand what went wrong
✅ **Timestamp** - Know when error occurred (ISO format)
✅ **HTTP Status Codes Correct** - 400, 404, 500 as appropriate
✅ **Professional Format** - Clean and organized

---

## 📋 Field Order in Response

The fields appear in this order in Postman:
1. `error` - Error code
2. `message` - Error description  
3. `status` - HTTP status code
4. `timestamp` - Error timestamp

---

## 🎯 Summary

Your error responses now follow the exact format you requested:

```json
{
    "error": "BOOKING_NOT_CONFIRMED",
    "message": "Booking is not CONFIRMED. Current status: PENDING",
    "status": 400,
    "timestamp": "2026-04-18T22:30:00"
}
```

**All 4 fields present** ✅
**Clean format** ✅
**Professional appearance** ✅

---

## 📚 Files Modified

✅ **SimpleErrorResponseDTO.java** - Updated to 4 fields
✅ **GlobalExceptionHandler.java** - Updated all 6 handlers

---

**Status**: ✅ Code updated and ready
**Next**: Compile and test in Postman!

