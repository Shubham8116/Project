# TechNest - E-Commerce Web Application – Spring Boot (MVC Architecture)
### This is a full-stack E-commerce web application built using Spring Boot (MVC), Thymeleaf, and Java 21. It offers a complete shopping experience with secure authentication, product browsing, cart management, and administrative control—all rendered through server-side HTML templates.

## 🎯 Key Features

### - 🛒 **Shopping Cart**  
####  Add, update, and remove products from a session-based cart
### - 🔍 **Product Search & Filtering**  
####  Search by name, category, and price range with dynamic filtering
### - 👤 **User Authentication & Role-Based Access**  
####  Spring Security integration with separate roles for **Admin** and **User**
### - 🧑‍💼 **Admin Dashboard**  
#### Manage inventory, products, categories, and users
### - 🧑‍🔧 **User Dashboard**  
####  Update profile, view order history, cancel orders
### - 🧾 **Checkout Flow**  
####  Capture billing/shipping info and confirm orders
### - ✅ **Validation & Exception Handling**  
####  Form-level validation using annotations and global exception management
### - 🕵️‍♂️ **JPA Auditing**  
####  Automatically tracks entity creation and modification timestamps

## 🧰 Tech Stack

| Layer        | Technology    |
|--------------|---------------|
| Language     | Java 21       |
| Framework    | Spring Boot   |
| View Engine  | Thymeleaf     |
| ORM          | Hibernate (via Spring Data JPA) |
| Database     | MySQL         |
| Security     | Spring Security |
| Build Tool   | Maven         |
| Utility      | Lombok, JPA Auditing |

## 📦 Project Setup

### ✅ Prerequisites

- JDK 21+
- Maven Build Tool
- MySQL Server (8.x recommended)
- IDE: IntelliJ / Eclipse / VS Code

### Access the app at: http://localhost:8080

## 📁 Application Structure

| Component                  | Description                               |
|----------------------------|-------------------------------------------|
| `ProductController`        | Handles product listing, search, and filtering |
| `CartController`           | Manages cart operations               |
| `PlaceOrderController`     | Handles order confirmation            |
| `AdminDashboardController` | Admin dashboard for managing products/users |
| `UserDashboardController`  | User dashboard for profile and orders |
| `SecurityConfig`           | Spring Security setup with role-based access |
| `GlobalExceptionHandler`   | Global exception handling using `@ControllerAdvice` |
| `JpaConfig`                | JPA auditing setup for tracking entity changes |
| `Templates/`               | Thymeleaf HTML views                  |

---
## 🔐 Security Overview

#### - Spring Security with form-based login
#### - Role-based access control (`ROLE_ADMIN`, `ROLE_USER`)
#### - Protected endpoints and dashboard segregation
#### - Password encryption using BCrypt

---
## 📈 Future Enhancements

#### - 💳 Integrate payment gateway (Razorpay)
#### - 📧 Send email/SMS confirmation via JavaMailSender or Twilio
#### - 🐳 Dockerize for deployment
#### - 📊 Add product reviews and ratings

## 📚 Learning Outcomes

### This project is ideal for:

#### - Practicing Spring Boot MVC architecture
#### - Implementing secure, role-based access
#### - Building full-stack apps with Thymeleaf
#### - Structuring scalable Java web applications
#### - Applying Hibernate ORM, JPA auditing, validation, and exception handling
