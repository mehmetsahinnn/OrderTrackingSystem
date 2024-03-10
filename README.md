# Online Order Tracking System /// [🇬🇧 English](README.md) | [🇹🇷 Türkçe](README_TR.md)

This project is an online order tracking system developed using Spring Boot with Java. It allows customers to order various products and track their orders.

## Project Structure

The project is structured into several packages, each containing classes related to a specific functionality of the system.

- **com.github.mehmetsahinnn.onlineordertrackingsystem.order:** This package contains classes related to order management. It includes the Order entity, OrderController for handling HTTP requests, OrderService for business logic, and OrderRepository for database operations.

- **com.github.mehmetsahinnn.onlineordertrackingsystem.product:** This package contains classes related to product management. It includes the Product entity, ProductController for handling HTTP requests, ProductService for business logic, and ProductRepository for database operations.

- **com.github.mehmetsahinnn.onlineordertrackingsystem.customer:** This package contains classes related to customer management. It includes the Customer entity, CustomerController for handling HTTP requests, CustomerService for business logic, and CustomerRepository for database operations.

- **com.github.mehmetsahinnn.onlineordertrackingsystem.cart:** This package contains classes related to cart management. It includes the Cart entity, CartController for handling HTTP requests, CartService for business logic, and CartRepository for database operations.

- **com.github.mehmetsahinnn.onlineordertrackingsystem.cartitem:** This package contains classes related to cart item management. It includes the CartItem entity, and CartItemRepository for database operations.

- **com.github.mehmetsahinnn.onlineordertrackingsystem.security:** This package contains classes related to security configuration of the application.

## Setup

1. Clone the repository to your local machine.
2. Open the project in your preferred IDE (IntelliJ IDEA is recommended).
3. Update the `application.properties` file with your database credentials.
4. Run the `OnlineOrderTrackingSystemApplication` class to start the application.

## Usage

### Product Management

- To create a new product, send a POST request to `/api/products` with the product details in the request body.
- To retrieve all products, send a GET request to `/api/products`.
- To search for products based on a specific category and price range, send a GET request to `/api/products/category` with the category, minimum price, and maximum price as request parameters.
- To retrieve a product by its ID, send a GET request to `/api/products/{id}`.
- To update a product, send a PUT request to `/api/products/{id}` with the new product details in the request body.
- To delete a product, send a DELETE request to `/api/products/{id}`.

### Order Management

- To place a new order, send a POST request to `/api/orders` with the order details in the request body.
- To retrieve all orders, send a GET request to `/api/orders`.
- To retrieve an order by its ID, send a GET request to `/api/orders/track/{id}`.
- To update an order, send a PUT request to `/api/orders/{id}` with the new order details in the request body.
- To cancel an order and increase the product stock by the quantity of the order, send a DELETE request to `/api/orders/cancel/{id}`.
- To retrieve the estimated delivery date of an order by its ID, send a GET request to `/api/orders/track/{id}/estimatedDeliveryDate`.


### Customer Management

- To register a new customer, send a POST request to `/api/register` with the customer details in the request body.
- To login, send a POST request to `/api/login` with the customer's email and password in the request body.
- To retrieve all customers, send a GET request to `/api/customers`.


### Cart Management

- To add a product to the cart, send a POST request to `/api/cart/{productId}/{quantity}`.

## Testing

The project includes unit tests for the `OrderService`, `OrderController`, `ProductService`, `ProductController`, `CustomerService`, `CustomerController`, `CartService`, and `CartController` classes. You can run these tests using your IDE's built-in test runner.

## Security

The application uses Spring Security for basic security configuration. The SecurityConfig class configures the security filter chain, and the `PCrypt` class provides a `BCryptPasswordEncoder` bean for password encoding.

## Database

The application uses PostgreSQL as the database. The `application.properties` file contains the database configuration. The OrderRepository, ProductRepository, CustomerRepository, CartRepository, and CartItemRepository interfaces extend JpaRepository for CRUD operations.

## Logging

The application uses SLF4J for logging. The OrderService, ProductService, CustomerService, and CartService classes include a Logger instance for logging messages.

## Dependencies

The project uses several dependencies, including Spring Boot Starter for web, data JPA, security, and actuator, Thymeleaf for server-side Java template engine, Lombok for reducing boilerplate code, and PostgreSQL JDBC Driver for connecting with PostgreSQL database. These dependencies are managed using Maven and are specified in the `pom.xml` file.

## Docker

This application can be deployed using Docker, a platform for distributing applications. To use Docker, create a Dockerfile in the root directory of the project. The Dockerfile specifies how your Docker image will be created. You can build and run the Docker image using the following commands:


```
docker build -t online-order-tracking-system .

```
```
docker run -p 8000:8080 online-order-tracking-system
```

These commands run your application as a Docker container and listen on port 8000.
<hr>

# Table Examples 

## Cart Table                  

| id | customer_id | 
|----|-------------|
| 1  | 1           |
| 2  | 2           |
| 3  | 3           |
| 4  | 4           |
| 5  | 5           |

## CartItem Table  

| id | cart_id | product_id | quantity |
|----|---------|------------|----------|
| 2  | 1       | 1          | 2        |
| 3  | 1       | 2          | 1        |
| 4  | 2       | 3          | 1        |
| 5  | 3       | 1          | 3        |

## Customer Table  

| id | name    | surname  | password | email                    | isadmin |
|----|---------|----------|----------|--------------------------|---------|
| 1  | John    | Doe      | password1| john.doe@example.com     | false   |
| 2  | Jane    | Doe      | password2| jane.doe@example.com     | false   |
| 3  | Alice   | Smith    | password3| alice.smith@example.com  | false   |
| 4  | Bob     | Johnson  | password4| bob.johnson@example.com  | false   |
| 5  | Charlie | Brown    | password5| charlie.brown@example.com| false   |

## Order Table  

| id | customerid | productid | quantity | status     | orderdate                     | estimateddeliverydate |
|----|------------|-----------|----------|------------|--------------------------------|----------------------|
| 1  | 1          | 2         | 1        | SHIPPED    | 2022-02-01 00:00:00.000000   | 2022-02-01           |
| 2  | 2          | 1         | 3        | DELIVERED  | 2022-03-01 00:00:00.000000   | 2022-04-01           |
| 3  | 2          | 3         | 1        | CANCELLED  | 2022-04-01 00:00:00.000000   | 2022-05-01           |
| 4  | 3          | 2         | 2        | CANCELLED  | 2022-05-01 00:00:00.000000   | 2022-06-01           |
| 5  | 3          | 3         | 4        | CONFIRMED  | 2024-03-15 13:30:00.000000   | 2025-05-01           |
| 6  | 3          | 3         | 4        | CONFIRMED  | 2024-03-15 13:30:00.000000   | 2025-05-01           |
| 7  | 3          | 3         | 4        | CONFIRMED  | 2024-03-15 13:30:00.000000   | 2025-05-01           |
| 8  | 3          | 3         | 4        | DELIVERED  | 2024-03-15 13:30:00.000000   | 2025-05-01           |
| 9  | 1          | 1         | 4        | CONFIRMED  | 2024-03-15 13:30:00.000000   | 2025-05-01           |

### Product Table

| id | name       | description       | price | numberinstock | category   |
|----|------------|-------------------|-------|---------------|------------|
| 1  | Product 1  | This is product 1| 19.99 | 22            | Category 1 |
| 2  | Product 2  | This is product 2| 29.99 | 29            | Category 3 |
| 3  | Product 3  | This is product 3| 39.99 | 25            | Category 1 |
| 4  | Product 4  | This is product 4| 49.99 | 23            | Category 3 |
| 5  | Product 5  | This is product 5| 59.99 | 31            | Category 3 |
| 6  | Product 6  | This is product 6| 60.99 | 26            | Category 1 |
| 7  | Product 7  | This is product 7| 70.99 | 17            | Category 2 |
| 8  | Product 8  | This is product 8| 80.99 | 38            | Category 3 |
| 9  | Product 9  | This is product 9| 19.99 | 19            | Category 1 |
| 10 | Product 10 | This is product 10 | 119.99 | 26            | Category 10|





