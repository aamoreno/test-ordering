# Order Management Service

A Spring Boot application for managing orders.

## Technology Stack

- Java 21
- Spring Boot 3.2.3
- Spring Data JPA
- H2 Database
- Maven

## Features

- RESTful API for order management
- CRUD operations for orders
- Filter orders by customer and status
- Data validation
- In-memory H2 database for development

## Getting Started

### Prerequisites

- Java 21
- Maven

### Running the Application

1. Clone the repository:
   ```
   git clone https://github.com/yourusername/test-ordering.git
   cd test-ordering
   ```

2. Build the application:
   ```
   mvn clean install
   ```

3. Run the application:
   ```
   mvn spring-boot:run
   ```

4. The application will be available at `http://localhost:8080`
   - H2 Console: `http://localhost:8080/h2-console`
   - API Endpoint: `http://localhost:8080/api/orders`

## API Endpoints

- `GET /api/orders` - Get all orders
- `GET /api/orders/{id}` - Get order by ID
- `GET /api/orders/customer/{customerId}` - Get orders by customer ID
- `GET /api/orders/status/{status}` - Get orders by status
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