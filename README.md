# Library Management System

A comprehensive Library Management System designed to efficiently manage books, users, loans, and categories. This project includes robust functionality for administrators, staff, and members, enabling seamless book lending, tracking, and reporting.

## Features

- **Book Management**: Add, update, delete, and search for books.
- **User Roles**:
  - **Admin**: Full access to all functionalities.
  - **Employee**: Manage loans and members.
  - **Member**: Borrow books, view loan history, and update personal details.
- **Loan Management**: Create, update, and track book loans with return deadlines.
- **Reporting**: Generate statistics for books, authors, publishers, and loan activities.

## Technologies Used

- **Backend**: Java (Spring Boot)
- **Database**: Relational Database Management System (PostgreSQL)
- **API**: RESTful Endpoints for communication

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/nihalersoy/library-project.git
   cd library-project

2. Configure the database connection in application.properties
3. Build and run the application
4. Access the application at http://localhost:8080

## Endpoints

/books: Manage book records.
/loans: Handle book loans.
/users: Manage user accounts and roles.
/report: Generate library statistics.
