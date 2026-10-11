# College Library Management System - REST API Documentation

This document describes all RESTful endpoints exposed by the Spring Boot backend (`http://localhost:8080/api`).

---

## 1. Dashboard API

### Get Dashboard Statistics
- **URL**: `/api/dashboard/stats`
- **Method**: `GET`
- **Description**: Returns live aggregated counts for the dashboard cards.
- **Success Response (200 OK)**:
```json
{
  "totalBooks": 10,
  "totalStudents": 45,
  "currentlyIssuedBooks": 4,
  "availableBooks": 32
}
```

---

## 2. Books API

### Get All Books / Search Books
- **URL**: `/api/books` or `/api/books?search={query}`
- **Method**: `GET`
- **Description**: Fetches all books or searches books by title or author.
- **Success Response (200 OK)**:
```json
[
  {
    "id": 1,
    "title": "Operating System Concepts",
    "author": "Abraham Silberschatz",
    "isbn": "978-1118063330",
    "category": "Computer Science",
    "totalCopies": 5,
    "availableCopies": 4
  }
]
```

### Get Book by ID
- **URL**: `/api/books/{id}`
- **Method**: `GET`
- **Success Response (200 OK)**:
```json
{
  "id": 1,
  "title": "Operating System Concepts",
  "author": "Abraham Silberschatz",
  "isbn": "978-1118063330",
  "category": "Computer Science",
  "totalCopies": 5,
  "availableCopies": 4
}
```
- **Error Response (404 Not Found)**:
```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Book not found with ID: 1"
}
```

### Add a New Book
- **URL**: `/api/books`
- **Method**: `POST`
- **Request Body**:
```json
{
  "title": "Clean Code",
  "author": "Robert C. Martin",
  "isbn": "978-0132350884",
  "category": "Computer Science",
  "totalCopies": 6,
  "availableCopies": 6
}
```
- **Success Response (201 Created)**: Returns the saved Book object with generated ID.
- **Error Response (409 Conflict)**: If ISBN already exists in the system.

### Update an Existing Book
- **URL**: `/api/books/{id}`
- **Method**: `PUT`
- **Request Body**: Same as POST.
- **Success Response (200 OK)**: Returns the updated Book object.

### Delete a Book
- **URL**: `/api/books/{id}`
- **Method**: `DELETE`
- **Success Response (200 OK)**:
```json
{
  "message": "Book deleted successfully."
}
```
- **Error Response (400 Bad Request)**:
```json
{
  "status": 400,
  "error": "Invalid Operation",
  "message": "Cannot delete book 'Clean Code' because copies are currently issued to students."
}
```

---

## 3. Students API

### Get All Students / Search
- **URL**: `/api/students` or `/api/students?search={query}`
- **Method**: `GET`
- **Success Response (200 OK)**:
```json
[
  {
    "id": 1,
    "name": "Arjun Verma",
    "studentId": "CS202601",
    "email": "arjun.verma@college.edu",
    "department": "Computer Science and Engineering"
  }
]
```

### Get Student by ID
- **URL**: `/api/students/{id}`
- **Method**: `GET`
- **Success Response (200 OK)**: Returns the Student object.

### Add a Student
- **URL**: `/api/students`
- **Method**: `POST`
- **Request Body**:
```json
{
  "name": "Pooja Sharma",
  "studentId": "EC202602",
  "email": "pooja.sharma@college.edu",
  "department": "Electronics and Communication"
}
```
- **Success Response (201 Created)**: Returns saved Student object.
- **Error Response (409 Conflict)**: If student ID or email already exists.

### Update a Student
- **URL**: `/api/students/{id}`
- **Method**: `PUT`
- **Request Body**: Same as POST.
- **Success Response (200 OK)**: Returns updated Student object.

### Delete a Student
- **URL**: `/api/students/{id}`
- **Method**: `DELETE`
- **Success Response (200 OK)**: Returns success message.
- **Error Response (400 Bad Request)**: If student has unreturned books.

---

## 4. Book Issues & Returns API

### Get All Loan Records
- **URL**: `/api/issues`
- **Method**: `GET`
- **Description**: Returns all loan records sorted by issue date descending.
- **Success Response (200 OK)**:
```json
[
  {
    "id": 1,
    "book": {
      "id": 1,
      "title": "Operating System Concepts",
      "author": "Abraham Silberschatz",
      "isbn": "978-1118063330"
    },
    "student": {
      "id": 1,
      "name": "Arjun Verma",
      "studentId": "CS202601",
      "department": "Computer Science and Engineering"
    },
    "issueDate": "2026-10-10",
    "dueDate": "2026-10-24",
    "returnDate": null,
    "status": "ISSUED"
  }
]
```

### Get Active Issues Only
- **URL**: `/api/issues/active`
- **Method**: `GET`
- **Description**: Returns only currently active loans where status is "ISSUED".

### Issue a Book to a Student
- **URL**: `/api/issues`
- **Method**: `POST`
- **Request Body**:
```json
{
  "studentId": 1,
  "bookId": 1,
  "dueDate": "2026-10-24"
}
```
- **Success Response (201 Created)**: Returns the newly created BookIssue record with status `"ISSUED"`.
- **Error Response (400 Bad Request)**: If book has 0 available copies (`BookUnavailableException`).

### Return an Issued Book
- **URL**: `/api/issues/{id}/return`
- **Method**: `PUT`
- **Description**: Marks the loan as returned, sets `returnDate` to today, and increments `availableCopies` of the book by 1.
- **Success Response (200 OK)**:
```json
{
  "id": 1,
  "issueDate": "2026-10-10",
  "dueDate": "2026-10-24",
  "returnDate": "2026-10-10",
  "status": "RETURNED"
}
```
- **Error Response (400 Bad Request)**: If book issue has already been marked as returned.
