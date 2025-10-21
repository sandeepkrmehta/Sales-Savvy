# Sales Savvy - E-Commerce Platform

A full-stack e-commerce application built with Spring Boot backend and modern security features including JWT authentication, Razorpay payment integration, and comprehensive order management.

## 🚀 Features

### User Management
- **Authentication & Authorization**
  - JWT-based token authentication
  - Role-based access control (ADMIN/CUSTOMER)
  - Secure password encryption using BCrypt
  - Token expiration and validation

### Product Management
- CRUD operations for products
- Category-based filtering
- Price range filtering
- Product search functionality
- Review system
- Image URL support

### Shopping Cart
- Add/remove items
- Update quantities
- Persistent cart storage
- Real-time price calculations

### Order Management
- Razorpay payment integration
- Order creation and tracking
- Order status management (CREATED, PAID, CANCELLED, SHIPPED, DELIVERED)
- Order history for customers
- Admin order management panel
- Optimistic locking for concurrent updates

### Payment Processing
- Razorpay payment gateway integration
- Secure payment signature verification
- Order summary generation
- Transaction tracking

## 🛠️ Technology Stack

### Backend
- **Framework**: Spring Boot 3.5.0
- **Language**: Java 17
- **Security**: Spring Security + JWT
- **Database**: MySQL 8.0.38
- **ORM**: Hibernate/JPA
- **Payment**: Razorpay Java SDK
- **Validation**: Jakarta Bean Validation
- **Connection Pooling**: HikariCP

### Build Tool
- Maven

## 📋 Prerequisites

- Java 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher
- Razorpay account (for payment integration)

## ⚙️ Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/sandeepkrmehta/sales-savvy.git
cd sales-savvy/sales-savvy-be/com.salesSavvy
```

### 2. Database Setup
Create a MySQL database:
```sql
CREATE DATABASE ecom;
```

### 3. Environment Configuration
Copy the example environment file:
```bash
cp .env.example .env
```

Edit `.env` and configure your credentials:
```properties
# Database Configuration
DB_URL=jdbc:mysql://localhost:3306/ecom
DB_USERNAME=root
DB_PASSWORD=your_database_password

# Razorpay Configuration
RAZORPAY_KEY_ID=your_razorpay_key_id
RAZORPAY_KEY_SECRET=your_razorpay_key_secret

# JWT Configuration (minimum 256 bits/32 characters)
JWT_SECRET=your_very_long_and_secure_random_secret_key_here_minimum_256_bits

# Admin Configuration
ADMIN_PASSWORD=your_secure_admin_password
```

**⚠️ Important**: 
- The `JWT_SECRET` must be at least 256 bits (32 characters) for security
- Never commit your `.env` file to version control

### 4. Build the Project
```bash
./mvnw clean install
```

### 5. Run the Application
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## 🔐 Security Configuration

### JWT Token Configuration
- **Token Expiration**: 24 hours (configurable)
- **Algorithm**: HS256
- **Minimum Key Length**: 256 bits

### Password Security
- BCrypt password hashing
- Minimum 8 characters required
- Encrypted before storage

### Role-Based Access Control
- **ADMIN**: Full access to all endpoints
- **CUSTOMER**: Limited access to customer-specific features

## 📚 API Endpoints

### Authentication
```
POST   /signUp          - Register new user
POST   /signIn          - Login and get JWT token
```

### Products (Public)
```
GET    /getAllProducts                  - Get all products
GET    /searchProduct?id={id}          - Search by ID
GET    /searchProducts?keyword={word}  - Search by name
GET    /products/category/{category}   - Get by category
GET    /categories                      - Get all categories
GET    /products/price-range            - Filter by price
GET    /products/sorted?order=asc       - Sort by price
GET    /products/filter                 - Advanced filtering
```

### Products (Admin Only)
```
POST   /addProduct              - Add new product
PUT    /updateProduct/{id}      - Update product
DELETE /deleteProduct/{id}      - Delete product
```

### Cart (Authenticated)
```
POST   /addToCart               - Add item to cart
GET    /getCart/{username}      - Get user's cart
DELETE /cart/item/{itemId}      - Remove item
PUT    /cart/item/{itemId}      - Update quantity
```

### Orders (Customer)
```
POST   /api/orders/place                      - Place new order
GET    /api/orders/user/{userId}              - Get user orders
GET    /api/orders/razorpay/{razorpayOrderId} - Get order by Razorpay ID
PUT    /api/orders/user/cancel/{razorpayOrderId} - Cancel order
```

### Orders (Admin)
```
GET    /api/orders/all                        - Get all orders
GET    /api/orders/db/{id}                    - Get order by DB ID
PUT    /api/orders/admin/update-status/{razorpayOrderId} - Update status
```

### Payment
```
POST   /payment/create                - Create Razorpay order
POST   /payment/verify                - Verify payment
GET    /order/{razorpayOrderId}       - Get order details
GET    /order/summary/{razorpayOrderId} - Get order summary
```

### Users (Admin Only)
```
GET    /user                   - Get all users
GET    /user/{username}        - Get user by username
PUT    /user/{id}              - Update user
DELETE /deleteUser/{id}        - Delete user
```

## 🔑 Authentication Flow

### 1. Sign Up
```json
POST /signUp
{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "securepass123",
  "role": "CUSTOMER",
  "gender": "Male",
  "dob": "1990-01-01"
}
```

### 2. Sign In
```json
POST /signIn
{
  "username": "johndoe",
  "password": "securepass123"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIs...",
  "username": "johndoe",
  "role": "CUSTOMER",
  "message": "Login successful"
}
```

### 3. Using the Token
Add the token to request headers:
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIs...
```

## 💳 Payment Integration

### Razorpay Setup
1. Create account at [Razorpay](https://razorpay.com)
2. Get API keys from Dashboard
3. Add keys to `.env` file

### Payment Flow
1. **Create Order**: `POST /payment/create`
2. **User Pays**: Using Razorpay Checkout
3. **Verify Payment**: `POST /payment/verify`
4. **Get Order**: `GET /order/{razorpayOrderId}`

## 🗃️ Database Schema

### Main Entities
- **Users**: User accounts with roles
- **Products**: Product catalog
- **Cart**: Shopping carts with items
- **Orders**: Order records with payment info
- **CartItem**: Individual items in carts/orders

### Key Relationships
- Users → Cart (One-to-One)
- Users → Orders (One-to-Many)
- Cart → CartItems (One-to-Many)
- Orders → CartItems (One-to-Many)
- Products → CartItems (One-to-Many)

## 📝 Logging

Logs are written to:
- **Console**: INFO level and above
- **File**: `logs/sales-savvy.log` (DEBUG level)
  - Max size: 10MB
  - History: 30 days

## 🧪 Testing

Run tests:
```bash
./mvnw test
```

## 🔧 Development

### Enable Hot Reload
Spring Boot DevTools is included for automatic restart during development.

### Disable Security (Development Only)
Comment out `@EnableWebSecurity` in `SecurityConfig.java` (not recommended for production)

## 🚀 Deployment

### Production Checklist
- [ ] Set strong `JWT_SECRET` (minimum 256 bits)
- [ ] Use production database credentials
- [ ] Set `spring.jpa.hibernate.ddl-auto=validate`
- [ ] Disable SQL logging (`spring.jpa.show-sql=false`)
- [ ] Configure proper CORS origins
- [ ] Use HTTPS for all endpoints
- [ ] Set up proper error monitoring
- [ ] Configure rate limiting
- [ ] Enable production logging levels

### Environment Variables
Set these in your production environment:
```bash
export DB_URL=jdbc:mysql://prod-db-host:3306/ecom
export DB_USERNAME=prod_user
export DB_PASSWORD=prod_password
export RAZORPAY_KEY_ID=prod_razorpay_key
export RAZORPAY_KEY_SECRET=prod_razorpay_secret
export JWT_SECRET=prod_jwt_secret_minimum_256_bits
export ADMIN_PASSWORD=prod_admin_password
```

## 🤝 Contributing

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors

- **Sandeep kumar mehta** - *Initial work*

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- Razorpay for payment gateway integration
- All contributors and testers

## 📞 Support

For support, email sandeepmehta.tech@zohomail.in or create an issue in the repository.

## 🐛 Known Issues

- None currently reported

## 🔮 Future Enhancements

- [ ] Email notifications
- [ ] Product reviews and ratings
- [ ] Wishlist functionality
- [ ] Advanced search with filters
- [ ] Order tracking with real-time updates
- [ ] Admin analytics dashboard
- [ ] Multi-currency support
- [ ] Social media authentication
- [ ] Product recommendations
- [ ] Inventory management

---

**Note**: This is a backend-only project. For frontend integration, configure CORS settings in `WebConfig.java` to match your frontend URL.
