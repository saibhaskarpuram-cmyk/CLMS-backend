# College Library Management System - Viva Questions & Answers

This guide is designed for quick reference during academic project presentations and viva voce examinations.

---

### Q1. What is React.js?
**Answer:** React is an open-source JavaScript library developed by Meta (Facebook) for building user interfaces. It uses a component-based architecture where the UI is broken into reusable, self-contained components. React uses a Virtual DOM to efficiently update only the parts of the webpage that changed without reloading the whole page.

---

### Q2. What is Spring Boot?
**Answer:** Spring Boot is an open-source Java framework built on top of the Spring framework. It simplifies Java backend development by offering **Auto-configuration**, an embedded server (Apache Tomcat), and starter dependencies. It allows developers to build production-ready REST APIs quickly with minimal XML or boilerplate configuration.

---

### Q3. What is MySQL?
**Answer:** MySQL is an open-source Relational Database Management System (RDBMS). It organizes structured data into tables consisting of rows and columns. It enforces relational integrity through Primary Keys, Foreign Keys, and Unique constraints, and is queried using Structured Query Language (SQL).

---

### Q4. What is CRUD?
**Answer:** CRUD stands for **Create, Read, Update, and Delete**. These are the four fundamental operations performed on database records:
- **Create**: Add a new book or student record (`POST`).
- **Read**: View book list, student details, or stats (`GET`).
- **Update**: Edit book information or student details (`PUT`).
- **Delete**: Remove a book or student from the system (`DELETE`).

---

### Q5. What is a REST API?
**Answer:** A REST (Representational State Transfer) API is an architectural style for network communication. It allows the frontend and backend to communicate over HTTP using standard methods (`GET`, `POST`, `PUT`, `DELETE`). Data is exchanged in a standardized format, usually JSON. REST is stateless, meaning each request contains all the information needed to process it.

---

### Q6. What is Maven?
**Answer:** Apache Maven is a build automation and dependency management tool for Java projects. It uses an XML configuration file called `pom.xml` (Project Object Model) to download required libraries from the Maven Central repository, compile code, execute automated tests, and package the application into an executable `.jar` file.

---

### Q7. What is JPA (Java Persistence API / Jakarta Persistence)?
**Answer:** JPA is a standard Java specification that defines how Java objects (Entities) are mapped and persisted to relational database tables. JPA provides standard annotations such as `@Entity`, `@Id`, `@Table`, and `@Column`. It is a specification, meaning it defines the rules and interfaces, but requires an implementation like Hibernate to perform the actual work.

---

### Q8. What is Hibernate?
**Answer:** Hibernate is an Object-Relational Mapping (ORM) framework that serves as the default implementation of JPA in Spring Boot. It maps Java classes to database tables and translates high-level Java object operations into low-level SQL queries automatically, saving developers from writing manual JDBC queries.

---

### Q9. What is Dependency Injection (DI) and Inversion of Control (IoC)?
**Answer:** Dependency Injection is a design pattern in which the Spring framework creates and supplies (injects) dependent objects into a class rather than the class creating them itself using the `new` keyword. Inversion of Control is the broader principle where control of object creation and lifecycle is handed over to the Spring IoC container. In our project, we use **constructor-based dependency injection** for clean, testable code.

---

### Q10. What are the Controller, Service, and Repository layers?
**Answer:** Our backend follows a 3-tier architecture:
1. **Controller Layer (`@RestController`)**: Handles incoming HTTP requests, validates request inputs, and sends back HTTP responses with appropriate status codes.
2. **Service Layer (`@Service`)**: Encapsulates business logic and rules (such as checking copy availability, verifying active loans before deletion, and coordinating transactions).
3. **Repository Layer (`@Repository`)**: Interacts directly with the database using Spring Data JPA to perform queries and database operations.

---

### Q11. What do the main Spring Boot annotations in this project do?
**Answer:**
- `@SpringBootApplication`: Marks the main entry point and enables auto-configuration, component scanning, and configuration bean definitions.
- `@RestController`: Marks a class as a controller where every method returns JSON data directly to the client instead of rendering an HTML view.
- `@RequestMapping("/api/...")`: Configures the base URL path for all endpoints within a controller.
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping`: Map HTTP GET, POST, PUT, and DELETE requests to specific Java methods.
- `@Service`: Marks a class holding business logic.
- `@Repository`: Marks a Data Access Object interface extending `JpaRepository`.
- `@Entity`: Designates a Java class as a JPA entity mapped to a database table.
- `@Id` & `@GeneratedValue`: Specify the primary key and its automatic generation strategy (`IDENTITY`).
- `@ManyToOne`: Defines a many-to-one foreign key relationship (e.g., many loan records relate to one Book).
- `@Transactional`: Ensures that all database operations in a method succeed together; if any error occurs, all changes are rolled back.
- `@Valid` & `@NotBlank`: Trigger automatic input validation and ensure required fields are not empty.
- `@RestControllerAdvice` & `@ExceptionHandler`: Intercept exceptions globally across all controllers and return structured error JSON responses.

---

### Q12. How are book records stored in the database?
**Answer:** Book records are stored in the `books` table with columns: `id` (Primary Key, auto-increment), `title`, `author`, `isbn` (Unique constraint), `category`, `total_copies`, and `available_copies`. The entity mapping is defined in `Book.java`.

---

### Q13. How does the book issue and return process work?
**Answer:**
1. **Issue Process**:
   - The user selects a student and a book with `availableCopies > 0`.
   - The `@Transactional` method in `BookIssueService`:
     - Checks if copies are available.
     - Decrements `availableCopies` by 1.
     - Creates a `BookIssue` record with `issueDate = today`, `dueDate`, and `status = "ISSUED"`.
     - Persists both records.
2. **Return Process**:
   - The user clicks "Return Book" on an active issue.
   - The system checks if the book was already returned.
   - It sets `returnDate = today` and `status = "RETURNED"`.
   - It increments `availableCopies` of the book by 1.
   - Both changes are saved atomically.

---

### Q14. What prevents a book from being deleted if it is currently issued?
**Answer:** Before deleting a book, `BookService.deleteBook(id)` queries `BookIssueRepository.existsByBookIdAndStatus(id, "ISSUED")`. If any active loans exist, it throws an `InvalidOperationException`, preventing accidental deletion of borrowed books. The same check protects students with active loans.

---

### Q15. How does React communicate with Spring Boot?
**Answer:** React uses **Axios** (an HTTP client) to send asynchronous HTTP requests (GET, POST, PUT, DELETE) to the Spring Boot REST API endpoints running on `http://localhost:8080/api`. The data is transferred in JSON format. Responses and errors are received and updated in React state using hooks (`useState`, `useEffect`).

---

### Q16. What is CORS and how is it handled?
**Answer:** CORS stands for **Cross-Origin Resource Sharing**. Browsers block web pages from making API requests to a different domain or port (e.g., React on port 5173 calling Spring Boot on port 8080) for security reasons. We configured CORS in `WebConfig.java` using Spring's `WebMvcConfigurer` to explicitly allow requests originating from `http://localhost:5173`.

---

### Q17. How does Spring Boot connect to MySQL?
**Answer:** Spring Boot connects to MySQL using the **MySQL Connector/J** JDBC driver and HikariCP connection pooling configured in `application.properties`:
- `spring.datasource.url`: The JDBC connection URL (`jdbc:mysql://localhost:3306/library_management`).
- `spring.datasource.username` and `spring.datasource.password`: MySQL credentials.
- `spring.jpa.hibernate.ddl-auto=update`: Automatically creates or updates table schemas to match JPA entity classes.

---

### Q18. Why use environment variables for database credentials?
**Answer:** Hardcoding passwords in code is a serious security risk, especially when pushing to public platforms like GitHub. Environment variables allow sensitive credentials (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`) to be injected at runtime without storing passwords in the repository.

---

### Q19. How are live statistics calculated on the Dashboard?
**Answer:** The `DashboardService` runs live aggregation queries against Spring Data JPA:
- `bookRepository.count()` for total books.
- `studentRepository.count()` for registered students.
- `bookIssueRepository.countByStatus("ISSUED")` for currently borrowed books.
- `bookRepository.sumAvailableCopies()` for total physical copies ready for borrowing.
The React dashboard consumes these numbers via `GET /api/dashboard/stats`.

---

### Q20. What is Vite and why was it chosen over Create React App?
**Answer:** Vite is a next-generation frontend build tool and dev server. It leverages native ES modules in the browser to provide near-instantaneous development server startup and lightning-fast Hot Module Replacement (HMR), compared to legacy webpack-based Create React App.

---

### Q21. What is React Router and how is navigation handled?
**Answer:** React Router is the standard routing library for React. It enables **client-side routing**, allowing the user to navigate between views (Dashboard, Books, Students, Issues) without reloading the page. It maps URL routes to React components using `<BrowserRouter>`, `<Routes>`, and `<Route>`.

---

### Q22. What happens if a duplicate ISBN is entered?
**Answer:**
1. The database table has a `unique` constraint on `isbn`.
2. Before saving, `BookService` calls `bookRepository.existsByIsbn(isbn)`.
3. If it exists, it throws a custom `DuplicateResourceException`.
4. `GlobalExceptionHandler` catches this exception and returns HTTP status 409 Conflict with a clear message.
5. The React frontend displays the error message in an alert banner.

---

### Q23. What is the role of the Dockerfile in the backend?
**Answer:** The `Dockerfile` provides a containerization recipe:
- **Build stage**: Uses a Maven container (`eclipse-temurin-21`) to compile source code and package the `.jar`.
- **Run stage**: Copies only the built `.jar` file to a lightweight Alpine Linux JRE image, exposing port 8080. This guarantees identical execution across development, testing, and cloud hosting platforms like Render.

---

### Q24. How is the project uploaded to GitHub?
**Answer:** The project is prepared as two clean, independent repositories:
1. `backend/`: Contains Java source code, `pom.xml`, `.gitignore`, and `Dockerfile`.
2. `frontend/`: Contains React source code, `package.json`, and `.gitignore`.
Each is initialized with `git init`, files staged with `git add .`, committed with `git commit -m "Initial commit"`, linked to remote GitHub repositories with `git remote add origin <URL>`, and pushed with `git push -u origin main`.

---

### Q25. How is the project deployed to production?
**Answer:**
1. **Database**: Hosted on a cloud MySQL provider (such as Aiven, PlanetScale, or Railway).
2. **Backend**: Deployed on Render as a Web Service using either Docker or Native Java 21, with environment variables `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`, and `CORS_ALLOWED_ORIGINS` configured in the dashboard.
3. **Frontend**: Deployed on Render, Vercel, or Netlify as a Static Site, pointing `VITE_API_BASE_URL` to the deployed backend URL.
