# 💼 Job Application Tracker

This is a full stack web application for tracking job applications with basic CRUD operations and filtering capabilities.

## 💻 Tech Stack
### Back-end
- **Java (Spring Boot)** – Used for building the RESTful API and handling business logic.
- **MySQL** – Relational database for persisting job application records.
- **JPA (Java Persistence API)** – Used for ORM (Object-Relational Mapping) to interact with the database through the `JpaRepository` interface for simplified CRUD operations.
- **Maven** – Dependency management and project build automation.

### Front-end
- **HTML5** - For web app structure, including forms and tables for job applications.
- **CSS3** - For styling the web interface.
- **JavaScript (Vanilla JS)** - Used to handle dynamic interactions on client side.

### Tools & Utilities
- **Postman** – For testing and verifying API endpoints during development.
- **Chart.js** - For data visualization.

## ✨ Features
- **Create, Read, Update, Delete (CRUD)** job applications.
- Filter job applications by:
  - Searching by keyword
  - Status
  - Location
  - Job Type (Onsite, Remote, Hybrid)
- Uses MySQL as the database to store job applications.
- Import/Export in CSV format.

## 🛢 Database Schema
The `job_application` table includes:
- `id` (Primary Key, Auto Increment)
- `company` (String)
- `position` (String)
- `status` (ENUM)
- `application_date` (LocalDate)
- `notes` (String)
- `jobType` (ENUM: ONSITE, REMOTE, HYBRID)
- `location` (String)

## ⚙️ Setup
1. Clone the repository.
2. Configure your `application.properties` to connect to your MySQL database.
3. Run the application using:
   ```bash
   mvn spring-boot:run
   ```
