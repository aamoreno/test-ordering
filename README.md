# Order Management Service

A Spring Boot application for managing orders.

## Technology Stack

- Java 21
- Spring Boot 3.2.3
- Spring Data JPA
- MySQL Database
- Maven

## Features

- RESTful API for order management
- CRUD operations for orders
- Filter orders by customer and status
- Data validation
- MySQL database with persistent storage

## Getting Started

### Prerequisites

- Java 21
- Maven
- MySQL Server

### Running the Application

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/test-ordering.git
   cd test-ordering
   ```

2. Set up MySQL:
   - Ensure MySQL server is running
   - The application will automatically create a database called 'orders'
   - Default credentials (root with no password) are used in the sample config
   - To use different credentials, update `application.properties`

3. Build the application:
   ```
   mvn clean install
   ```

4. Run the application:
   ```
   mvn spring-boot:run
   ```

5. The application will be available at `http://localhost:8080`
   - API Endpoint: `http://localhost:8080/api/orders`

## API Endpoints

- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `POST /api/orders` - Create a new order
- `PUT /api/orders/{id}` - Update an order
- `DELETE /api/orders/{id}` - Delete an order

## Sample Request

```json
POST /api/orders
{
  "customerId": "customer123",
  "productId": "product456",
  "quantity": 2
}
```

## Project Structure

```
src
├── main
│   ├── java
│   │   └── com
│   │       └── example
│   │           └── ordering
│   │               ├── OrderingApplication.java
│   │               ├── controller
│   │               │   └── OrderController.java
│   │               ├── dto
│   │               │   └── OrderDto.java
│   │               ├── exception
│   │               │   └── GlobalExceptionHandler.java
│   │               ├── mapper
│   │               │   └── OrderMapper.java
│   │               ├── model
│   │               │   └── Order.java
│   │               ├── repository
│   │               │   └── OrderRepository.java
│   │               └── service
│   │                   └── OrderService.java
│   └── resources
│       └── application.properties
└── test
    └── java
        └── com
            └── example
                └── ordering
                    ├── controller
                    │   └── OrderControllerTest.java
                    └── service
                        └── OrderServiceTest.java
```