   # Product Management API

## Overview

This is a **Product Management API** built with **Quarkus**. The API provides full **CRUD** (Create, Read, Update, Delete) functionality for managing products, using **Hibernate Reactive**, **MySQL**, and a custom **ApiResponse<T>** DTO for consistent responses.

The application is designed to be lightweight, reactive, and optimized for high concurrency scenarios.

## Features

- Full CRUD operations for managing products
- Custom **ApiResponse<T>** DTO for consistent API responses
- Integration with **MySQL** via **Hibernate Reactive**
- Reactive and non-blocking architecture for better scalability
- Built using **Quarkus** for fast startup and low memory usage
- **Swagger** integration for API documentation and testing

## Technologies

- **Quarkus**: Java framework for building lightweight, high-performance applications
- **Hibernate Reactive**: For reactive database interactions
- **MySQL**: Relational database for storing product information
- **RESTEasy**: For building RESTful web services
- **JAX-RS**: For handling REST endpoints
- **Swagger UI**: For API documentation and interactive testing

## Getting Started

Follow these steps to get the project up and running locally.

### Prerequisites

- JDK 17 or higher
- Maven 3.x or higher
- MySQL 8.x or higher

### Setup

1. Clone the repository:

   ```bash
   git clone https://github.com/JadhavShravan/apibanking-product-mangement.git
   cd apibanking-product-mangement

2. Check Database :
 ```bash
   quarkus.datasource.db-kind=mysql
   quarkus.datasource.username=your-username
   quarkus.datasource.password=your-password
   quarkus.datasource.jdbc.url=jdbc:mysql://localhost:3306/products
   quarkus.hibernate-orm.database.generation=update
 
 **make sure you replaced database details with your credentiales**
3. Run below commands:
```bash
  ./mvnw clean install  
  ./mvnw compile quarkus:dev

4. Access Swagger UI:
**Test API using swagger**
```bash
http://localhost:8080/swagger-ui

5. Run Unit Tests:
```bash
 ./mvnw test



