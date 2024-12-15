Study Notion - Education Platform

Study Notion is a comprehensive education platform built using Spring Boot and SQL Database. The platform enables users to access courses, manage profiles, upload documents, and much more. This README provides an overview of the APIs, their functionality, and how to interact with them.

Table of Contents

Project Overview

Technologies Used

Installation

API Endpoints

Authentication

Contact

Profile Management

Admin Operations

Course Management

Category Management

Section and Subsection Management

Ratings and Reviews

Contributing

License

Project Overview

Study Notion is an ed-tech platform designed to facilitate seamless interaction between instructors and students. The platform supports user authentication, course creation, document uploads, and more, ensuring a robust educational experience.

Technologies Used

Backend: Spring Boot, Spring Security

Database: SQL (MySQL/PostgreSQL)

Authentication: JWT-based authentication

Tools: Postman for API testing

Installation

Clone the repository:

git clone https://github.com/your-repo/study-notion.git

Navigate to the project directory:

cd study-notion

Configure the database connection in application.properties.

Build and run the application:

./mvnw spring-boot:run

API Endpoints

Authentication

Endpoint

Method

Description

/auth/signup

POST

Sign up a new user.

/auth/login

POST

Login and generate a JWT.

/auth/otp

POST

Generate OTP for email.

/public/ok

GET

Health checker endpoint.

Example: Sign Up

POST http://localhost:8081/auth/signup
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "password123",
  "accountTypes": "STUDENT",
  "phoneNumber": "1234567890",
  "otp": "123456"
}

Contact

Endpoint

Method

Description

/contact-us/query

POST

Submit a contact query.

Example: Contact Query

POST http://localhost:8081/contact-us/query
{
  "firstName": "Jane",
  "lastName": "Doe",
  "email": "jane.doe@example.com",
  "message": "I cannot access my course."
}

Profile Management

Endpoint

Method

Description

/user/profile-update

PUT

Update user profile.

/student/information-update

PUT

Update student information.

/user/profile-get

GET

Fetch user profile details.

Admin Operations

Endpoint

Method

Description

/admin/get-all-user

GET

Retrieve all users.

Course Management

Endpoint

Method

Description

/instructor/course/create/{id}

POST

Create a new course.

/instructor/course/thumbnail-upload/{id}

PUT

Upload a course thumbnail.

/instructor/course/update/{id}

PUT

Update course details.

/instructor/course/delete/{id}

DELETE

Delete a course.

Example: Create Course

POST http://localhost:8081/instructor/course/create/675e8c6d7aa77a56bb9b1b85
{
  "courseName": "Java Programming",
  "courseDescription": "Learn Java from scratch.",
  "whatYouWillLearn": "Basics to advanced Java concepts.",
  "price": 10000,
  "tags": ["#java", "#programming"],
  "instructions": ["Focus on concepts", "Practice daily"],
  "status": "PUBLISHED"
}

Category Management

Endpoint

Method

Description

/admin/category/create

POST

Create a new category.

/admin/category/show-all

GET

Retrieve all categories.

/admin/category/update/{id}

PUT

Update a category.

/admin/category/delete/{id}

DELETE

Delete a category.

Section and Subsection Management

Endpoint

Method

Description

/instructor/section/create

POST

Create a section.

/instructor/section/update/{id}

PUT

Update a section.

/instructor/section/delete/{id}

DELETE

Delete a section.

/instructor/sub-section/create

POST

Create a subsection.

/instructor/sub-section/update/{id}

PUT

Update a subsection.

/instructor/sub-section/delete/{id}

DELETE

Delete a subsection.

Ratings and Reviews

Endpoint

Method

Description

/rating-review/create

POST

Create a rating or review.

Contributing

Contributions are welcome! Please fork this repository and submit a pull request for any features, bug fixes, or enhancements.

License

This project is licensed under the MIT License. See the LICENSE file for details.

