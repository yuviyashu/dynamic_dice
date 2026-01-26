# Dynamic Dice API

Dynamic Dice – Backend APIs is a backend-only, microservices-based application that exposes secure REST APIs for user onboarding, authentication, and dynamic dice management. The system is frontend-agnostic and can be integrated with any UI framework such as React, Angular, Vue, mobile apps, or other client technologies.

## 🎯 Features

- **User Management**: Admin and User registration with OTP-based verification
- **Authentication**: JWT-based secure authentication system
- **Dynamic Dice Creation**: Admins can create custom dice with 6 faces and images
- **Dice Rolling**: Users can roll dice and get random results
- **Image Upload**: Upload and manage dice face images
- **Role-based Access Control**: Admin and User roles with specific permissions
- **Email Notifications**: OTP delivery via email
- **API Documentation**: Swagger/OpenAPI documentation included

## 🛠 Tech Stack

- **Backend**: Spring Boot 3.2.5
- **Language**: Java 17
- **Database**: MongoDB
- **Authentication**: JWT (JSON Web Tokens)
- **Build Tool**: Gradle
- **Documentation**: Swagger/OpenAPI 3.0
- **Containerization**: Docker & Docker Compose
- **Email**: Spring Mail (Gmail SMTP)

## 📋 Prerequisites

Before you begin, ensure you have the following installed:

- **Java 17** or higher
- **Gradle 8.5** or higher
- **MongoDB 6.0** or higher
- **Docker** and **Docker Compose** (for containerized deployment)
- **Git** for version control

## 🚀 Quick Start

### Option 1: Using Docker Compose (Recommended)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd dynamic_dice
   ```

2. **Start the application**
   ```bash
   docker-compose up --build
   ```

3. **Access the application**
   - API Base URL: `http://localhost:8082`
   - Swagger UI: `http://localhost:8082/swagger-ui.html`
   - MongoDB: `localhost:27017`

### Option 2: Local Development Setup

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd dynamic_dice
   ```

2. **Start MongoDB**
   ```bash
   # Using Docker
   docker run -d -p 27017:27017 --name mongo mongo:6.0
   
   # Or install MongoDB locally
   # Follow MongoDB installation guide for your OS
   ```

3. **Configure application**
   - Update `src/main/resources/application.yaml` with your MongoDB connection details
   - Configure email settings for OTP functionality

4. **Build and run the application**
   ```bash
   # Using Gradle wrapper
   ./gradlew clean build
   ./gradlew bootrun
   
   # Or using your local Gradle
   gradle clean build
   gradle bootrun
   ```

5. **Access the application**
   - API Base URL: `http://localhost:8082`
   - Swagger UI: `http://localhost:8082/swagger-ui.html`

## 📚 API Documentation

### Authentication Endpoints

#### Admin Registration
```http
POST /auth/admin/register/request-otp
Content-Type: application/json

{
  "email": "admin@example.com"
}
```

```http
POST /auth/admin/register/verify-otp
Content-Type: application/json

{
  "email": "admin@example.com",
  "otp": "123456",
  "password": "securePassword"
}
```

#### User Registration
```http
POST /auth/user/register/request-otp
Content-Type: application/json

{
  "userEmail": "user@example.com",
  "adminEmail": "admin@example.com"
}
```

```http
POST /auth/user/register/verify-otp
Content-Type: application/json

{
  "userEmail": "user@example.com",
  "adminEmail": "admin@example.com",
  "otp": "123456",
  "userPassword": "userPassword"
}
```

#### Login
```http
POST /auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password"
}
```

### Admin Endpoints

#### Create Dice
```http
POST /api/admin/dice
Authorization: Bearer <jwt-token>
Content-Type: application/json

{
  "diceName": "My Custom Dice",
  "faces": [
    {
      "value": "1",
      "imageUrl": "http://example.com/face1.png"
    },
    {
      "value": "2",
      "imageUrl": "http://example.com/face2.png"
    },
    {
      "value": "3",
      "imageUrl": "http://example.com/face3.png"
    },
    {
      "value": "4",
      "imageUrl": "http://example.com/face4.png"
    },
    {
      "value": "5",
      "imageUrl": "http://example.com/face5.png"
    },
    {
      "value": "6",
      "imageUrl": "http://example.com/face6.png"
    }
  ]
}
```

#### Get Admin Dice
```http
GET /api/admin/dice
Authorization: Bearer <jwt-token>
X-ADMIN-ID: <admin-id>
```

#### Upload Image
```http
POST /api/admin/images/upload
Authorization: Bearer <jwt-token>
Content-Type: multipart/form-data

file: <image-file>
```

### User Endpoints

#### Get Available Dice
```http
GET /api/user/dice
Authorization: Bearer <jwt-token>
X-ADMIN-ID: <admin-id>
```

#### Roll Dice
```http
POST /api/user/dice/roll/{diceId}
Authorization: Bearer <jwt-token>
```

## 🔧 Configuration

### Environment Variables

The application can be configured using the following environment variables:

```yaml
# Server Configuration
server:
  port: 8082

# MongoDB Configuration
spring:
  data:
    mongodb:
      uri: mongodb://localhost:27017/dynamicdice
      database: dice

# Email Configuration (Gmail SMTP)
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true

# File Upload Configuration
file:
  upload-dir: uploads/dice
```

### Email Setup

To enable OTP functionality, you need to configure Gmail SMTP:

1. Enable 2-factor authentication on your Gmail account
2. Generate an App Password
3. Update the email configuration in `application.yaml`

## 🏗 Project Structure

```
src/main/java/com/dynamic/dice/
├── controller/          # REST API controllers
│   ├── AdminDiceController.java
│   ├── AdminRegistrationController.java
│   ├── ImageUploadController.java
│   ├── LoginController.java
│   ├── UserDiceController.java
│   └── UserRegistrationController.java
├── dto/                 # Data Transfer Objects
├── enums/              # Enumerations
├── exception/          # Custom exceptions
├── filter/             # Security filters
├── handler/            # Exception handlers
├── model/              # Database entities
│   ├── Dice.java
│   ├── DiceFace.java
│   ├── DiceRollLog.java
│   ├── OtpRequest.java
│   └── User.java
├── repository/         # MongoDB repositories
├── service/            # Business logic
└── utils/              # Utility classes
```

## 🎮 Usage Flow

1. **Admin Registration**: Admin registers with OTP verification
2. **User Registration**: Users register with admin approval via OTP
3. **Login**: Both admin and users login to get JWT tokens
4. **Dice Creation**: Admin creates custom dice with 6 faces and images
5. **Dice Rolling**: Users roll dice to get random results

## 🔒 Security Features

- **JWT Authentication**: Secure token-based authentication
- **Role-based Access Control**: Admin and User roles
- **Password Encryption**: BCrypt password hashing
- **OTP Verification**: Email-based OTP for registration
- **Input Validation**: Request validation using Spring Validation

## 📝 Business Rules

- Each admin can create maximum 3 dice
- Each dice must have exactly 6 faces
- Each dice face must have a value and an image
- Users can only roll dice created by their admin
- OTP expires in 5 minutes
- File upload size limited to 5MB per file

## 🐳 Docker Deployment

The application includes Docker support:

- **Dockerfile**: Multi-stage build for production deployment
- **docker-compose.yml**: Complete stack with MongoDB
- **Volume mounting**: Persistent data storage for MongoDB and uploaded files

## 🧪 Testing

Run the test suite:

```bash
# Run all tests
./gradlew test

# Run tests with coverage
./gradlew test jacocoTestReport
```

## 📈 Monitoring

The application includes Spring Boot Actuator for monitoring:

- Health checks: `http://localhost:8082/actuator/health`

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Support

If you encounter any issues or have questions:

1. Check the [Swagger UI](http://localhost:8082/swagger-ui.html) for API documentation
2. Review the application logs for error details
3. Ensure all prerequisites are properly configured
4. Create an issue in the repository with detailed information

## 🔄 Version History

- **v0.0.1-SNAPSHOT**: Initial release with core functionality
  - User authentication and registration
  - Dice creation and management
  - Image upload functionality
  - JWT-based security
