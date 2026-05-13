# API Testing Examples with Exception Handling

## Base URL
```
http://localhost:8080/api/itinerary
```

---

## ✅ Success Cases

### 1. Generate Itinerary (Valid Request)
```bash
curl -X POST http://localhost:8080/api/itinerary/generate/1 \
  -H "Content-Type: application/json"
```

**Success Response (201 Created):**
```json
{
  "itineraryId": 1,
  "userId": 1,
  "startDate": "2024-05-15",
  "endDate": "2024-05-20",
  "status": "CONFIRMED"
}
```

---

### 2. Get Itinerary (Valid Request)
```bash
curl -X GET http://localhost:8080/api/itinerary/1 \
  -H "Content-Type: application/json"
```

**Success Response (200 OK):**
```json
{
  "itineraryId": 1,
  "userId": 1,
  "startDate": "2024-05-15",
  "endDate": "2024-05-20",
  "status": "CONFIRMED"
}
```

---

### 3. Health Check
```bash
curl -X GET http://localhost:8080/api/itinerary/health
```

**Success Response (200 OK):**
```
Itinerary API is running!
```

---

## ❌ Error Cases

### 1. Invalid User ID (Zero)
```bash
curl -X POST http://localhost:8080/api/itinerary/generate/0 \
  -H "Content-Type: application/json"
```

**Error Response (400 Bad Request):**
```json
{
  "timestamp": "2024-05-12T10:30:45.123456",
  "status": 400,
  "error": "INVALID_USER_ID",
  "message": "User ID must be a positive number",
  "path": "/api/itinerary/generate/0"
}
```

---

### 2. Invalid User ID (Negative)
```bash
curl -X POST http://localhost:8080/api/itinerary/generate/-5 \
  -H "Content-Type: application/json"
```

**Error Response (400 Bad Request):**
```json
{
  "timestamp": "2024-05-12T10:30:50.654321",
  "status": 400,
  "error": "INVALID_USER_ID",
  "message": "User ID must be a positive number",
  "path": "/api/itinerary/generate/-5"
}
```

---

### 3. Booking Not Found
```bash
curl -X POST http://localhost:8080/api/itinerary/generate/999 \
  -H "Content-Type: application/json"
```

**Error Response (404 Not Found):**
```json
{
  "timestamp": "2024-05-12T10:31:20.654321",
  "status": 404,
  "error": "BOOKING_NOT_FOUND",
  "message": "Booking not found for User ID: 999",
  "path": "/api/itinerary/generate/999"
}
```

---

### 4. Booking Status Not Confirmed
Assuming booking with userId 2 has status "PENDING":

```bash
curl -X POST http://localhost:8080/api/itinerary/generate/2 \
  -H "Content-Type: application/json"
```

**Error Response (400 Bad Request):**
```json
{
  "timestamp": "2024-05-12T10:32:15.789012",
  "status": 400,
  "error": "INVALID_BOOKING_STATUS",
  "message": "Booking is not CONFIRMED. Current status: PENDING",
  "path": "/api/itinerary/generate/2"
}
```

---

### 5. Reservation Not Found
Assuming booking exists but has no associated reservation:

```bash
curl -X POST http://localhost:8080/api/itinerary/generate/3 \
  -H "Content-Type: application/json"
```

**Error Response (404 Not Found):**
```json
{
  "timestamp": "2024-05-12T10:32:45.123456",
  "status": 404,
  "error": "RESERVATION_NOT_FOUND",
  "message": "Reservation not found for Booking ID: 5",
  "path": "/api/itinerary/generate/3"
}
```

---

### 6. Itinerary Not Found (Get)
```bash
curl -X GET http://localhost:8080/api/itinerary/999 \
  -H "Content-Type: application/json"
```

**Error Response (404 Not Found):**
```json
{
  "timestamp": "2024-05-12T10:33:20.456789",
  "status": 404,
  "error": "ITINERARY_NOT_FOUND",
  "message": "Itinerary not found for User ID: 999",
  "path": "/api/itinerary/999"
}
```

---

## 🧪 Testing Workflow

### Step 1: Verify API is Running
```bash
curl -X GET http://localhost:8080/api/itinerary/health
```
Expected: 200 OK with message "Itinerary API is running!"

---

### Step 2: Test Invalid Input
```bash
curl -X POST http://localhost:8080/api/itinerary/generate/0
```
Expected: 400 Bad Request with INVALID_USER_ID error

---

### Step 3: Test Non-existent Data
```bash
curl -X POST http://localhost:8080/api/itinerary/generate/999
```
Expected: 404 Not Found with BOOKING_NOT_FOUND error

---

### Step 4: Test Valid Request (assuming valid data exists)
```bash
curl -X POST http://localhost:8080/api/itinerary/generate/1
```
Expected: 201 Created with itinerary details

---

### Step 5: Retrieve Generated Itinerary
```bash
curl -X GET http://localhost:8080/api/itinerary/1
```
Expected: 200 OK with itinerary details

---

## 📊 Error Code Reference

| Error Code | HTTP Status | Meaning |
|-----------|------------|---------|
| INVALID_USER_ID | 400 | User ID is null or ≤ 0 |
| INVALID_BOOKING_STATUS | 400 | Booking status is not "CONFIRMED" |
| BOOKING_NOT_FOUND | 404 | No booking found for the user |
| RESERVATION_NOT_FOUND | 404 | No reservation found for the booking |
| ITINERARY_NOT_FOUND | 404 | No itinerary found for the user |
| INTERNAL_SERVER_ERROR | 500 | Unexpected server error |

---

## 🔧 Using Postman

### 1. Create a Collection

1. Open Postman
2. Click "New" → "Collection"
3. Name it "Travel360 API Tests"
4. Click "Create"

---

### 2. Add Requests

#### Request 1: Health Check
- **Method**: GET
- **URL**: `http://localhost:8080/api/itinerary/health`
- **Expected**: 200 OK

#### Request 2: Generate Itinerary (Valid)
- **Method**: POST
- **URL**: `http://localhost:8080/api/itinerary/generate/1`
- **Expected**: 201 Created

#### Request 3: Generate Itinerary (Invalid ID)
- **Method**: POST
- **URL**: `http://localhost:8080/api/itinerary/generate/0`
- **Expected**: 400 Bad Request with INVALID_USER_ID

#### Request 4: Generate Itinerary (Not Found)
- **Method**: POST
- **URL**: `http://localhost:8080/api/itinerary/generate/999`
- **Expected**: 404 Not Found with BOOKING_NOT_FOUND

#### Request 5: Get Itinerary (Valid)
- **Method**: GET
- **URL**: `http://localhost:8080/api/itinerary/1`
- **Expected**: 200 OK

#### Request 6: Get Itinerary (Not Found)
- **Method**: GET
- **URL**: `http://localhost:8080/api/itinerary/999`
- **Expected**: 404 Not Found with ITINERARY_NOT_FOUND

---

## 📝 Response Headers to Check

All responses include these standard headers:

```
Content-Type: application/json
Date: Mon, 12 May 2024 10:30:45 GMT
Server: Apache Tomcat/10.1.x
```

Error responses will have:
- **Status Code**: 400, 404, or 500
- **Content-Type**: application/json
- **Body**: ErrorResponseDTO in JSON format

---

## 🐛 Debugging Tips

1. **Check Logs**: Look for SEVERE level logs when exceptions occur
2. **Verify Status Code**: Always check the HTTP status code in the response
3. **Parse JSON**: Error responses are valid JSON, parse them to see the error message
4. **Check Timestamp**: Error timestamp helps correlate with server logs
5. **Review Path**: The path field shows which endpoint caused the error

---

## 💾 Sample Database Data for Testing

To test the application, you need:

### Booking Table
```sql
INSERT INTO booking (booking_id, user_id, partner_id, item_type, booking_date, status, amount)
VALUES (1, 1, 101, 'HOTEL', '2024-05-01', 'CONFIRMED', 5000.00);

INSERT INTO booking (booking_id, user_id, partner_id, item_type, booking_date, status, amount)
VALUES (2, 2, 102, 'FLIGHT', '2024-05-02', 'PENDING', 15000.00);
```

### Reservation Table
```sql
INSERT INTO reservation (reservation_id, booking_id, start_date, end_date)
VALUES (1, 1, '2024-05-15', '2024-05-20');
```

With this data:
- `POST /generate/1` → 201 Created (CONFIRMED booking with reservation)
- `POST /generate/2` → 400 Bad Request (PENDING booking)
- `POST /generate/999` → 404 Not Found (no booking)
- `POST /generate/0` → 400 Bad Request (invalid ID)

---

## ✅ Checklist for Testing

- [ ] API starts without errors
- [ ] Health endpoint returns 200 OK
- [ ] Invalid user ID returns 400 with INVALID_USER_ID
- [ ] Missing booking returns 404 with BOOKING_NOT_FOUND
- [ ] Non-confirmed booking returns 400 with INVALID_BOOKING_STATUS
- [ ] Missing reservation returns 404 with RESERVATION_NOT_FOUND
- [ ] Valid request returns 201 with itinerary data
- [ ] Get valid itinerary returns 200 OK
- [ ] Get non-existent itinerary returns 404 with ITINERARY_NOT_FOUND
- [ ] All error responses have timestamp, status, error, message, path fields
- [ ] All responses are valid JSON
- [ ] Check application logs show SEVERE errors and INFO messages

---

**Happy Testing! 🚀**

