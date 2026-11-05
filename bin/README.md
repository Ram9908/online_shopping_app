# Online Shopping Application

Spring Boot 3.2 · Java 17 · Spring MVC + JSP · Spring Data JPA · MySQL (or in-memory H2)

Roles: **user** (browse, cart, checkout, orders), **vendor** (manage own products, image upload), **admin** (view users, vendors, orders, products).

## Run it (pick one)

Requires JDK 17+ (Maven is bundled via `mvnw`).

### A. Zero setup (in-memory H2, data resets on restart)
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=h2      # Windows: mvnw.cmd ...
```

### B. MySQL in Docker
```bash
cp .env.example .env          # set DB_PASSWORD
docker compose up -d db
export DB_PASSWORD=<same value>   # Windows PowerShell: $env:DB_PASSWORD="..."
./mvnw spring-boot:run
```
Or run everything in containers: `docker compose up --build`.

Then open **http://localhost:9977** (redirects to the login page).

### Admin login
Set `ADMIN_EMAIL` / `ADMIN_PASSWORD`. If `ADMIN_PASSWORD` is empty, a random one is generated and printed in the startup log (`Generated admin login for this run -> ...`).
Register a normal user or vendor from the sign-up page.

### Tests
```bash
./mvnw test
```

## Configuration (environment variables)
| Variable | Default | Purpose |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3307/online_shopping_application?createDatabaseIfNotExist=true` | JDBC URL |
| `DB_USERNAME` | `root` | DB user |
| `DB_PASSWORD` | *(required for MySQL)* | DB password |
| `ADMIN_EMAIL` / `ADMIN_PASSWORD` | `admin@shop.local` / random | Admin account |
| `UPLOAD_DIR` | `uploads` | Vendor image folder, served at `/Images/**` |
| `PORT` | `9977` | HTTP port |

## What changed from the original
- Passwords stored as **BCrypt** hashes. Old plain-text accounts still log in once and are auto-upgraded.
- Login/registration use indexed lookups (`findFirstByEmail`) instead of loading every user and looping.
- No hardcoded admin or DB credentials; everything is configurable.
- Stock is **checked** when adding to cart and **reduced at checkout**, in one `@Transactional` method with row locking (`CheckoutService`). Previously stock dropped on add-to-cart and was never restored.
- Checkout no longer reloads all products per cart line and uses the address just entered.
- The cart/order/admin pages check the session; users can only touch their own cart and orders; admin pages need an admin login.
- Product image upload no longer writes to a hardcoded `C:\Users\...` path.
- Roles (101/102/103) are seeded on startup, so a fresh database works.
- Removed the shared mutable list fields from singleton controllers; admin pages no longer display passwords.
- Added unit tests for authentication and checkout (Mockito), alongside the original admin test.

## Known limitations / next steps
- Uses `spring-security-crypto` for hashing, not the full Spring Security filter chain: no CSRF protection yet.
- Cart update/delete and order cancel still use GET links (the JSP forms were left as-is). Convert to POST + CSRF token.
- Money is `Float`/`Integer`; use `BigDecimal` for real payments.
- Order rows reference products by id only and do not store quantity or price at time of purchase.
- `ddl-auto=update` is for development; use Flyway/Liquibase for production.
