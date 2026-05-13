# 🔄 HOW TO PROPERLY RECOMPILE & RESTART - Step-by-Step Guide

## ⚠️ Important: You Must Recompile & Restart!

The code has been updated, but **your running application is still using the old compiled version**. You need to:

1. **Stop the running application**
2. **Clean and recompile**
3. **Restart the application**

---

## 🛑 Step 1: Stop the Current Application

If the application is currently running in your terminal:

**Press**: `Ctrl + C`

This will stop the running Spring Boot application.

**Expected Output**:
```
Application terminated
```

---

## 🧹 Step 2: Clean & Compile

In your terminal, navigate to the project and run:

```bash
cd "C:\Users\2492245\OneDrive - Cognizant\Desktop\Travel360\Travel360_Project_DTO - Copy\travel360"
```

Then clean and compile:

```bash
mvn clean compile
```

**Expected Output**:
```
[INFO] Cleaning...
[INFO] Compiling...
[INFO] BUILD SUCCESS
```

**This is IMPORTANT**: The `clean` command removes all old compiled files and `compile` regenerates them with your new code changes.

---

## 🚀 Step 3: Start the Application Again

After successful compilation, start the application:

```bash
mvn spring-boot:run
```

**Expected Output**:
```
Started Travel360Application in X.XXX seconds
2026-05-13 XX:XX:XX - Hibernate: select...
=================================
     Travel360 Itinerary Module  
=================================
Enter User ID:
```

---

## 🧪 Step 4: Test in Postman

Now open Postman and test:

### Test 1: User Without Booking (ID: 113)

```
Request:
POST http://localhost:8080/api/itinerary/generate/113

Expected Response:
{
    "message": "Booking not found for User ID: 113"
}

Status: 404 Not Found ✅
```

### Test 2: User With PENDING Booking (ID: 103)

```
Request:
POST http://localhost:8080/api/itinerary/generate/103

Expected Response:
{
    "message": "Booking is not CONFIRMED. Current status: PENDING"
}

Status: 400 Bad Request ✅
```

### Test 3: User With CONFIRMED Booking (ID: 1)

```
Request:
POST http://localhost:8080/api/itinerary/generate/1

Expected Response:
{
    "itineraryId": 1,
    "userId": 1,
    "bookingId": 1,
    "startDate": "2024-05-15",
    "endDate": "2024-05-20",
    "status": "CONFIRMED"
}

Status: 201 Created ✅
```

---

## ✅ Complete Recompilation Script

**Run this complete sequence in one go**:

```bash
cd "C:\Users\2492245\OneDrive - Cognizant\Desktop\Travel360\Travel360_Project_DTO - Copy\travel360"
mvn clean compile
mvn spring-boot:run
```

Or if using Windows PowerShell:

```powershell
cd "C:\Users\2492245\OneDrive - Cognizant\Desktop\Travel360\Travel360_Project_DTO - Copy\travel360"; mvn clean compile; mvn spring-boot:run
```

---

## 🔍 Verification Checklist

After recompilation and restart, verify:

- [ ] Application started successfully
- [ ] No compilation errors
- [ ] No warnings
- [ ] Terminal shows "Started Travel360Application"
- [ ] Test in Postman with ID 113
- [ ] Response shows only `{"message": "..."}`
- [ ] No timestamp, status, error, or path fields
- [ ] Status code is 404
- [ ] Clean JSON format ✅

---

## 🐛 Troubleshooting

### Issue: Still Seeing Old Format (5 Fields)

**Cause**: Application hasn't been restarted with new code
**Solution**:
1. Stop current application (Ctrl + C)
2. Run `mvn clean compile` again
3. Start with `mvn spring-boot:run`
4. Test again in Postman

### Issue: Compilation Error

**Cause**: Missing dependencies or syntax issues
**Solution**:
1. Make sure SimpleErrorResponseDTO.java exists
2. Run `mvn clean` first
3. Then `mvn compile`
4. Check error messages carefully

### Issue: Port Already in Use

**Cause**: Previous instance still running
**Solution**:
1. Try Ctrl + C (may not work if terminal closed)
2. Open Task Manager
3. Find "java.exe" process
4. End task
5. Wait 10 seconds
6. Start application again

---

## 📋 Key Files That Changed

### New File Created
```
✅ travel360/src/main/java/org/example/travel360/dto/SimpleErrorResponseDTO.java
```

### File Modified  
```
✅ travel360/src/main/java/org/example/travel360/exception/GlobalExceptionHandler.java
```

### Files Unchanged
```
✅ ItineraryService.java
✅ ItineraryRestController.java
✅ ItineraryController.java
✅ All exception classes
```

---

## ⏱️ Expected Times

| Step | Time |
|------|------|
| Clean | 5-10 seconds |
| Compile | 10-20 seconds |
| Start App | 5-10 seconds |
| Total | ~30-40 seconds |

---

## 📝 What to Look For in Terminal After Restart

**Before** (Old):
```
2026-05-13 10:30:11 - GlobalExceptionHandler - Booking not found: ...
2026-05-13 10:30:11 - ItineraryController - Invalid booking status: ...
```

**After** (New):
```
Same logging (unchanged) ✅
But response body is simplified ✓
```

The logging stays the same, but the Postman response is now clean!

---

## 🎯 Quick Checklist

**Before Testing**:
- [ ] Application stopped (Ctrl + C)
- [ ] Ran `mvn clean compile` (saw BUILD SUCCESS)
- [ ] Ran `mvn spring-boot:run` (saw "Started Travel360Application")
- [ ] Waited for prompt to appear
- [ ] Opened fresh Postman request

**During Testing**:
- [ ] Sent POST to /api/itinerary/generate/113
- [ ] Response shows only `{"message": "..."}`
- [ ] No extra fields visible
- [ ] Status is 404

**Result**:
- [ ] ✅ Simple error format working!
- [ ] ✅ Postman looks clean!
- [ ] ✅ Professional response format!

---

## 💡 Pro Tips

1. **Always use `mvn clean`** - Don't just compile, clean first
2. **Wait for startup** - Don't test until terminal shows the prompt
3. **Use fresh Postman tab** - Browser cache might show old response
4. **Check status code** - Should show 404, not 200
5. **Look at Response tab** - Make sure you're viewing the response body

---

## ✨ After Successful Restart

You should see in Postman:

```json
{
    "message": "Booking not found for User ID: 113"
}
```

**NOT**:

```json
{
    "timestamp": "2026-05-13T10:30:11.295884",
    "status": 404,
    "error": "BOOKING_NOT_FOUND",
    "message": "Booking not found for User ID: 113",
    "path": "/api/itinerary/generate/113"
}
```

---

## 🚀 Let's Do This!

**Execute these commands NOW**:

```bash
# Terminal Command 1: Navigate to project
cd "C:\Users\2492245\OneDrive - Cognizant\Desktop\Travel360\Travel360_Project_DTO - Copy\travel360"

# Terminal Command 2: Stop current app (if running)
# Press Ctrl + C

# Terminal Command 3: Clean and compile
mvn clean compile

# Terminal Command 4: Run application
mvn spring-boot:run

# Then test in Postman
```

**After this, Postman will show the clean, simple error format!** ✅

---

**Status**: Ready for your action!
**Next**: Execute the commands above and test in Postman
**Result**: Clean error responses with only message field!

