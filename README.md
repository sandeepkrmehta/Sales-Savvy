# Sales-Savvy

Sales-Savvy is an e-commerce web application featuring both admin and customer roles. Built using Spring Boot, Hibernate, MySQL, and ReactJS, it allows seamless product management, customer shopping experience, and secure payments through Razorpay.

## Features

### Admin
- Register/Login as Admin
- Add new products
- Update product details
- Delete products
- Manage orders

### Customer
- Register/Login as Customer
- Browse products
- Add products to cart
- Manage cart items
- Fill in shipping and payment details
- Make payments via Razorpay
- Purchase products

## Tech Stack

- **Backend:** Spring Boot, Hibernate, MySQL
- **Frontend:** ReactJS
- **Payment Gateway:** Razorpay

## Getting Started

### Prerequisites

- Java 8+
- Node.js and npm
- MySQL database

### Backend Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/sandeepkrmehta/Sales-Savvy.git
   ```
2. Navigate to backend directory and install dependencies.
3. Set up MySQL and update `application.properties` with your DB credentials.
4. Run the Spring Boot application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend Setup

1. Navigate to the frontend directory.
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the React development server:
   ```bash
   npm start
   ```

## Usage

- Admin and customers can register and log in.
- Admins can manage products and orders.
- Customers can browse, add to cart, and purchase products using Razorpay.

## License

This project is licensed under the MIT License.

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Contact

- [GitHub Profile](https://github.com/sandeepkrmehta)
