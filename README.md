# University Course Management System

Welcome to the **University Course Management System**, a sleek, modern Java-based application designed to streamline course administration, student enrollment tracking, and instructor course management. Built with Swing for a rich GUI experience, this project combines functionality with a visually stunning interface to make academic management a breeze.

## Features

- **User Roles:**
  - **Admin:** Manage users and courses with a powerful dashboard.
  - **Instructor:** View assigned courses in a stylish interface.
  - **Student:** Check enrollments and grades effortlessly.

- **Login System:**
  - Secure authentication with a gradient-styled login window.
  - Role-based redirection to personalized dashboards.

- **Dashboards:**
  - **Admin Dashboard:**
    - View all users in a tabulated format.
    - Add new courses with name, credits, and instructor assignment.
    - Modern tabbed interface with real-time updates.
  - **Instructor Dashboard:**
    - Display assigned courses with course ID, name, and credits.
    - Interactive sidebar with placeholder features (Profile, Statistics).
  - **Student Dashboard:**
    - List enrolled courses with grades in a clean table layout.

- **Visual Design:**
  - Gradient backgrounds and rounded corners for a contemporary look.
  - Custom-styled tables, buttons, and input fields.
  - Hover effects and smooth typography using Segoe UI.

## Project Structure

UniversityCourseManagement/
├── src/
│   ├── db/
│   │   └── DatabaseConnection.java  # Database connection utility
│   ├── model/
│   │   └── User.java               # User model class
│   └── ui/
│       ├── LoginFrame.java         # Login window
│       ├── AdminDashboard.java     # Admin dashboard with course management
│       ├── InstructorDashboard.java # Instructor course view
│       └── StudentDashboard.java   # Student enrollment view
├── README.md                       # This file
└── database.sql                    # Sample database schema (optional)

## Prerequisites

- **Java 8+**: Ensure JDK is installed.
- **Database**: MySQL or any JDBC-compatible database.
- **Dependencies**: JDBC driver (e.g., `mysql-connector-java`).

## Setup Instructions

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/yourusername/UniversityCourseManagement.git
   cd UniversityCourseManagement

## Database Setup:
Create a database (e.g., university_db).

Execute the following schema (adjust as needed):
sql

CREATE TABLE Users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    role ENUM('Admin', 'Instructor', 'Student') NOT NULL,
    email VARCHAR(100) NOT NULL
);

CREATE TABLE Courses (
    course_id INT PRIMARY KEY AUTO_INCREMENT,
    course_name VARCHAR(100) NOT NULL,
    credits INT NOT NULL,
    instructor_id INT,
    FOREIGN KEY (instructor_id) REFERENCES Users(user_id)
);

CREATE TABLE Enrollments (
    enrollment_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id INT,
    course_id INT,
    grade DOUBLE,
    FOREIGN KEY (student_id) REFERENCES Users(user_id),
    FOREIGN KEY (course_id) REFERENCES Courses(course_id)
);

Update DatabaseConnection.java with your database credentials.

Compile and Run:
Compile the Java files:
bash

javac -cp .:lib/* src/ui/*.java src/db/*.java src/model/*.java

Run the application:
bash

java -cp .:lib/* ui.LoginFrame

Note: Replace lib/* with the path to your JDBC driver JAR if needed.

## Usage
Login:
Enter your username and password.

The system redirects you to the appropriate dashboard based on your role.

### Admin:
View all users in the "All Users" tab.

Switch to the "Courses" tab to add new courses with a name, credits, and optional instructor.

### Instructor:
See your assigned courses in a beautifully formatted table.

Explore the sidebar for future features.

Student:
Check your enrolled courses and grades in a sleek table.


Student Dashboard: Blue-gray simplicity for enrollment tracking.



## Contributing
Feel free to fork this repo and submit pull requests! Ideas for improvement:
UI tweaks (new color schemes, animations).

Additional features (e.g., scheduling, notifications).

Bug fixes or performance optimizations.

Credits
Built with love by WolfOf420Street using Java Swing and a lot of creativity. Special thanks to xAI's Grok for the design inspo and code assistance!

## License
This project is licensed under the MIT License - see the LICENSE file for details (create one if you want!).

