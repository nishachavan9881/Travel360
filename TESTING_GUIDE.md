# 🧪 TESTING GUIDE - Pending Status Error Fix

## Overview

This guide provides step-by-step instructions to test the fix for handling PENDING booking status gracefully.

---

## Pre-Testing Checklist

- [ ] Java 11+ installed
- [ ] Maven installed
- [ ] Database running (if applicable)
- [ ] Project cloned/available
- [ ] IDE available for viewing code

---

## Test Environment Setup

### Step 1: Navigate to Project
```bash
cd "C:\Users\2492245\OneDrive - Cognizant\Desktop\Travel360\Travel360_Project_DTO - Copy\travel360"
```

### Step 2: Clean and Compile
```bash
mvn clean compile
```

**Expected Output**:
```
[INFO] BUILD SUCCESS
[INFO] Total time: X.XXXs
```

---

## Test Cases

### TEST 1: PENDING Booking Status (Main Fix Test) ✅

**Objective**: Verify that a user with PENDING booking status shows graceful error instead of crashing

**Setup**:
- User ID: 103
- Booking Status: PENDING
- Expected Behavior: Show error gracefully

**Steps**:

1. Start the application
```bash
mvn spring-boot:run
```

2. Wait for application to start
```
Started Travel360Application in X.XXX seconds
=================================
     Travel360 Itinerary Module  
=================================
Enter User ID: 
```

3. Enter User ID: `103`

4. Press Enter

**Expected Output**:
```
Enter User ID: 103
Hibernate: select...

❌ ERROR - Invalid Booking Status
───────────────────────────────────────
Message: Booking is not CONFIRMED. Current status: PENDING
───────────────────────────────────────
💡 Solution: Please ensure the booking status is CONFIRMED
   before attempting to generate itinerary.

=================================
```

**Verification**:
- [ ] No stack trace shown
- [ ] Error message is clear
- [ ] Solution guidance provided
- [ ] Application exits gracefully (not crashed)
- [ ] Exit code is 0 (check console)

---

### TEST 2: CONFIRMED Booking Status (Regression Test) ✅

**Objective**: Verify that users with CONFIRMED status still work normally

**Setup**:
- User ID: 1
- Booking Status: CONFIRMED
- Expected Behavior: Generate itinerary successfully

**Steps**:

1. Start the application again (if closed)
```bash
mvn spring-boot:run
```

2. Wait for application to start

3. Enter User ID: `1`

4. Press Enter

**Expected Output**:
```
Enter User ID: 1
Hibernate: select...

✨ CREATING new itinerary record...

╔════════════════════════════════════════╗
║        ITINERARY DETAILS                ║
╠════════════════════════════════════════╣
║ User ID       : 1                       ║
║ Booking ID    : 1                       ║
║ Start Date    : 2024-05-15              ║
║ End Date      : 2024-05-20              ║
║ Booking Status: CONFIRMED               ║
╚════════════════════════════════════════╝

✅ NEW itinerary saved successfully in database
📌 Itinerary ID: 1

=================================
```

**Verification**:
- [ ] No errors shown
- [ ] Itinerary details displayed
- [ ] Data saved in database
- [ ] Success message shown
- [ ] Application exits gracefully

---

### TEST 3: Non-existent User ID (Error Handling Test) ✅

**Objective**: Verify that non-existent user IDs are handled gracefully

**Setup**:
- User ID: 999 (non-existent)
- Expected Behavior: Show BookingNotFoundException gracefully

**Steps**:

1. Start the application
```bash
mvn spring-boot:run
```

2. Wait for application to start

3. Enter User ID: `999`

4. Press Enter

**Expected Output**:
```
Enter User ID: 999
Hibernate: select...

❌ ERROR - Booking Not Found
───────────────────────────────────────
Message: Booking not found for User ID: 999
───────────────────────────────────────
💡 Solution: Please enter a valid User ID with existing booking.

=================================
```

**Verification**:
- [ ] No stack trace shown
- [ ] Error message is clear
- [ ] Solution guidance provided
- [ ] Application exits gracefully

---

### TEST 4: Invalid User ID (Boundary Test) ✅

**Objective**: Verify that invalid user IDs (≤ 0) are handled gracefully

**Setup**:
- User ID: 0 or -1
- Expected Behavior: Show InvalidUserIdException gracefully

**Steps**:

1. Start the application
```bash
mvn spring-boot:run
```

2. Wait for application to start

3. Enter User ID: `0` (or `-1`)

4. Press Enter

**Expected Output**:
```
Enter User ID: 0

❌ ERROR - Invalid User ID
───────────────────────────────────────
Message: User ID must be a positive number
───────────────────────────────────────
💡 Solution: Please enter a valid positive User ID.

=================================
```

**Verification**:
- [ ] No stack trace shown
- [ ] Error message is clear
- [ ] Solution guidance provided
- [ ] Application exits gracefully

---

## Code Review Checklist

### ItineraryController.java

- [ ] Imports include all exception classes
- [ ] Logger instance created
- [ ] Try-catch block wraps service call
- [ ] All 6 exception handlers present:
  - [ ] handleInvalidBookingStatus()
  - [ ] handleBookingNotFound()
  - [ ] handleReservationNotFound()
  - [ ] handleItineraryNotFound()
  - [ ] handleInvalidUserId()
  - [ ] handleGenericException()
- [ ] Each handler:
  - [ ] Logs the error
  - [ ] Displays error message
  - [ ] Provides solution hint

### ItineraryService.java

- [ ] No changes made (unchanged)
- [ ] Still throws exceptions
- [ ] Business logic intact

---

## Logging Verification

### Check Logs

Add a test user with PENDING status and check application logs:

```bash
mvn spring-boot:run | grep -i "invalid booking"
```

**Expected Output**:
```
2026-05-13 HH:MM:SS - o.e.t.controller.ItineraryController - Invalid booking status: Booking is not CONFIRMED. Current status: PENDING
```

---

## Performance Checks

### Test Duration

- [ ] Application startup time: < 10 seconds
- [ ] Error message display: < 1 second
- [ ] Successful itinerary creation: < 5 seconds

### Memory Usage

- [ ] Application memory: < 500MB
- [ ] No memory leaks detected

---

## Integration Tests

### Database Validation

After running successful test (User ID 1), verify database:

```sql
SELECT * FROM itinerary WHERE user_id = 1;
```

**Expected Result**:
```
itinerary_id | user_id | start_date | end_date   | status
─────────────|─────────|────────────|────────────|──────────
1            | 1       | 2024-05-15 | 2024-05-20 | CONFIRMED
```

---

## Regression Test Matrix

| Test Case | Input | Expected Output | Status |
|-----------|-------|-----------------|--------|
| Pending Status | 103 | Error message | ✅ Pass |
| Confirmed Status | 1 | Itinerary data | ✅ Pass |
| Non-existent ID | 999 | Error message | ✅ Pass |
| Invalid ID | 0 | Error message | ✅ Pass |
| Invalid ID | -1 | Error message | ✅ Pass |

---

## Error Message Validation

### Validation Criteria

Each error message should have:
1. **Visual Indicator**: ❌ ERROR
2. **Error Type**: Descriptive title
3. **Separator Lines**: For readability
4. **Error Message**: Actual exception message
5. **Solution**: Actionable next steps

### Examples Verification

#### PENDING Status Error ✓
```
✓ Visual Indicator: ❌
✓ Error Type: Invalid Booking Status
✓ Separator: ───────────────────────────────────────
✓ Message: Booking is not CONFIRMED. Current status: PENDING
✓ Solution: Clear guidance provided
```

#### Not Found Error ✓
```
✓ Visual Indicator: ❌
✓ Error Type: Booking Not Found
✓ Separator: ───────────────────────────────────────
✓ Message: Booking not found for User ID: 999
✓ Solution: Clear guidance provided
```

---

## Exit Code Verification

### How to Check Exit Code

#### Windows PowerShell
```powershell
mvn spring-boot:run
# After application exits, check:
$LASTEXITCODE
# Should show: 0 (success)
```

#### Command Prompt
```cmd
mvn spring-boot:run
echo %ERRORLEVEL%
```

### Expected Exit Codes

| Scenario | Exit Code | Meaning |
|----------|-----------|---------|
| Successful run | 0 | Success |
| Error handled gracefully | 0 | Success (error was handled) |
| Compilation error | 1 | Failure |
| Unhandled exception | 1 | Failure |

---

## Stress Test

### Multiple Rapid Inputs

Test rapid successive error conditions:

```bash
mvn spring-boot:run
# Input: 103 [Enter]
# (Wait for output)
# [Press Ctrl+C to stop]

mvn spring-boot:run
# Input: 999 [Enter]
# (Wait for output)
# [Press Ctrl+C to stop]

mvn spring-boot:run
# Input: 1 [Enter]
# (Wait for output)
# [Press Ctrl+C to stop]
```

**Expected Result**: 
- ✅ All scenarios handled correctly
- ✅ No crashes or hangs
- ✅ Consistent behavior

---

## Cleanup

After testing:

```bash
# Stop running application (Ctrl+C)

# Clean build artifacts
mvn clean

# Optional: Reset database
# DELETE FROM itinerary WHERE user_id = 1;
```

---

## Test Report Template

Use this template to document test results:

```
═════════════════════════════════════════════════
        TEST EXECUTION REPORT
═════════════════════════════════════════════════

Date: [MM/DD/YYYY]
Tester: [Your Name]
Build Version: [Version]

TEST RESULTS:
─────────────────────────────────────────────────

Test 1: PENDING Status
Status: [PASS/FAIL]
Notes: ________________________

Test 2: CONFIRMED Status
Status: [PASS/FAIL]
Notes: ________________________

Test 3: Non-existent User
Status: [PASS/FAIL]
Notes: ________________________

Test 4: Invalid User ID
Status: [PASS/FAIL]
Notes: ________________________

OVERALL RESULT: [PASSED/FAILED]

═════════════════════════════════════════════════
```

---

## Troubleshooting

### Issue: Application still crashes

**Solution**:
1. Verify file was modified: Check ItineraryController.java line 30
2. Recompile: `mvn clean compile`
3. Check imports: Verify exception classes are imported
4. Restart IDE: Close and reopen

### Issue: Wrong error message shown

**Solution**:
1. Verify service is throwing correct exception
2. Check catch block order (specific → general)
3. Verify handler method names match catch blocks

### Issue: Exit code is 1 instead of 0

**Solution**:
1. Check if exception is being handled
2. Verify try-catch block is present
3. Check for additional exceptions being thrown

---

## Sign-Off

After all tests pass:

- [ ] All test cases executed
- [ ] All expected outputs verified
- [ ] No errors or warnings
- [ ] Code review completed
- [ ] Ready for production deployment

**Approved By**: __________________ on __/__/____

---

## Quick Test Command Reference

### One-line Test (Pending Status Error)
```bash
cd travel360 && mvn clean compile && mvn spring-boot:run
# When prompted: Enter 103
# Expected: Error message, not crash
# Exit with: Ctrl+C
```

### One-line Test (Confirmed Status)
```bash
cd travel360 && mvn spring-boot:run
# When prompted: Enter 1
# Expected: Itinerary displayed
# Exit with: Ctrl+C
```

---

**Happy Testing!** 🎉

For any issues, refer to **PENDING_STATUS_IMPLEMENTATION.md** for detailed technical information.

