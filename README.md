# Online Shopping Application

A role-based e-commerce web application built with Spring Boot. Shoppers can browse products, manage a cart, and place orders. Vendors can manage their products, and administrators can review users, vendors, products, and orders.

## Technology

- Java 17
- Spring Boot 3.2
- Spring MVC and JSP
- Spring Data JPA
- MySQL or H2
- Maven Wrapper
- Docker Compose

## Features

- **Shopper:** browse products, manage a cart, check out, and view orders
- **Vendor:** manage products and upload product images
- **Admin:** view users, vendors, products, and orders
- Passwords are stored as BCrypt hashes
- Product stock is checked when items are added to the cart and reduced during checkout
- Roles are seeded when the application starts
- Database, admin account, image folder, and server port can be configured

## Run locally

You need JDK 17 or later. Maven is included through the Maven Wrapper.

### Option 1: Run with H2

H2 is an in-memory database. Its data resets when the application restarts.

**Windows PowerShell:**

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=h2"
```

**macOS or Linux:**

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=h2
```

### Option 2: Run with MySQL in Docker

1. Copy `.env.example` to `.env` and set your database password.
2. Start the database:

   ```bash
   docker compose up -d db
   ```

3. Set `DB_PASSWORD` in your terminal to the same password you put in `.env`.

   **Windows PowerShell:**

   ```powershell
   $env:DB_PASSWORD = "your-password"
   ```

   **macOS or Linux:**

   ```bash
   export DB_PASSWORD="your-password"
   ```

4. Start the application.

   **Windows PowerShell:**

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   **macOS or Linux:**

   ```bash
   ./mvnw spring-boot:run
   ```

To start the application and database in containers, run:

```bash
docker compose up --build
```

Open [http://localhost:9977](http://localhost:9977). The application redirects to the login page.

## Accounts

Set `ADMIN_EMAIL` and `ADMIN_PASSWORD` to configure the admin account. If `ADMIN_PASSWORD` is empty, the application generates a password and prints the login details in the startup log.

Register a shopper or vendor through the sign-up page.

## Configuration

| Variable | Default | Purpose |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3307/online_shopping_application?createDatabaseIfNotExist=true` | JDBC connection URL |
| `DB_USERNAME` | `root` | Database username |
| `DB_PASSWORD` | None | Database password; required for MySQL |
| `ADMIN_EMAIL` | `admin@shop.local` | Admin account email |
| `ADMIN_PASSWORD` | Randomly generated if empty | Admin account password |
| `UPLOAD_DIR` | `uploads` | Directory used for product images |
| `PORT` | `9977` | Application HTTP port |

## Tests

**Windows PowerShell:**

```powershell
.\mvnw.cmd test
```

**macOS or Linux:**

```bash
./mvnw test
```

## Engineering notes

- Checkout is transactional and locks product rows while checking and updating stock.
- Users can access only their own carts and orders; administrative pages require an admin login.
- Login and registration use indexed email lookups.
- Existing plain-text passwords are upgraded to BCrypt after a successful login.
- Product image uploads use a configurable directory instead of a machine-specific path.

## Current limitations

- The application uses Spring Security Crypto for password hashing, but not the full Spring Security filter chain. CSRF protection is not enabled.
- Cart updates, cart deletion, and order cancellation currently use GET links.
- Prices use `Float` or `Integer`; a real payment workflow should use `BigDecimal`.
- Order items do not save the quantity and price at the time of purchase.
- Hibernate schema auto-update is for development. A production deployment should use Flyway or Liquibase.
