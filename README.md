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
