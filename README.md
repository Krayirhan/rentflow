# RentFlow

RentFlow, Java 17 ile gelistirilmis katmanli mimariye sahip ve **masaüstü uygulaması** olarak tasarlanan bir arac kiralama sistemidir.

Bu proje; Java OOP, service-repository mimarisi, exception yonetimi, Maven, JUnit testleri, Spring Boot REST API, JavaFX GUI ve PostgreSQL database ogrenmek amaciyla gelistirilmistir.

**Sürüm 4.0**: PostgreSQL Production Database + JWT Authentication + Desktop Ready

## Ozellikler

- **REST API (Spring Boot)** ✨
  - Complete CRUD endpoints
  - Global exception handling
  - JWT authentication
- **Desktop GUI (JavaFX)** 🎨
  - Tabbed interface (Vehicle, Customer, Rental)
  - Real-time search and filtering
  - Price calculator
  - Status indicators
- **Production Database** 🗄️
  - PostgreSQL with Flyway migrations
  - H2 for development/testing
  - Connection pooling (HikariCP)
  - User authentication table
- **Vehicle Management**
  - Car and Motorcycle types
  - Daily pricing and rental fee calculation
  - Vehicle status tracking (Available/Rented)
- **Customer Management**
  - Customer registration and search
  - Phone number uniqueness
- **Rental Operations**
  - Rental creation with date validation
  - Automatic fee calculation
  - Return processing
  - Late fee tracking
  - Rental history by customer
- **Exception Handling**
  - Custom exception hierarchy
  - Global error responses
- **Security**
  - JWT token authentication
  - Role-based authorization (ADMIN/USER)
  - Password encryption (BCrypt)
  - Automatic tests (26 test cases)

## Kullanilan Teknolojiler

- Java 17
- Spring Boot 3.2
- Spring Data JPA
- PostgreSQL (Production)
- H2 (Development/Testing)
- Flyway (Database Migration)
- JavaFX 21
- Maven
- JUnit 5
- OOP
- Repository Pattern
- Service Layer Pattern
- REST API

## Proje Yapisi

```text
src/main/java/com/rentflow
├── api (Spring Boot REST)
│   ├── RentFlowRestApplication.java
│   ├── controller/
│   │   ├── AuthController.java
│   │   ├── VehicleRestController.java
│   │   ├── CustomerRestController.java
│   │   └── RentalRestController.java
│   ├── dto/
│   │   ├── LoginRequest.java
│   │   └── LoginResponse.java
│   └── handler/
│       └── GlobalExceptionHandler.java
├── app
│   ├── Main.java (Console - Legacy)
│   ├── RentFlowApplication.java (JavaFX GUI)
│   ├── DataInitializer.java
├── security (JWT & Auth)
│   └── JwtTokenProvider.java
├── config
│   └── PasswordEncoderConfig.java
├── service
│   ├── api/ (Interfaces)
│   │   ├── IVehicleService.java
│   │   ├── ICustomerService.java
│   │   └── IRentalService.java
│   ├── VehicleService.java
│   ├── CustomerService.java
│   └── RentalService.java
├── enums
│   ├── VehicleStatus.java
│   └── RentalStatus.java
├── exception (7 custom exceptions)
├── model (JPA entities)
│   ├── Vehicle.java (abstract)
│   ├── Car.java
│   ├── Motorcycle.java
│   ├── Customer.java
│   ├── Rental.java
│   └── User.java
├── repository
│   ├── jpa/ (Spring Data JPA)
│   └── memory/ (In-Memory implementations)
└── ui
    ├── console (Legacy)
    └── fx (JavaFX GUI)

resources/
├── application.properties
├── application-dev.properties
├── application-prod.properties
└── db/migration/ (Flyway scripts)
    ├── V1__Initial_Schema.sql
    ├── V2__Sample_Data.sql
    └── V3__Add_Users_Table.sql
```

## Katmanlar

### app

Uygulamanin baslangic noktasidir.

- Main
- DataInitializer

### model

Domain nesnelerini icerir.

- Vehicle
- Car
- Motorcycle
- Customer
- Rental

### enums

Sabit durum degerlerini icerir.

- VehicleStatus
- RentalStatus

### repository

Veri saklama ve bulma islemlerini yonetir.

### service

Is kurallarini yonetir.

- Arac validasyonlari
- Musteri validasyonlari
- Kiralama akisi
- Iade islemleri
- Fiyat hesaplama

### ui

Console arayuzunu yonetir.

- menu: Ana menu
- controller: Console aksiyonlari
- input: Kullanici input islemleri
- printer: Console cikti islemleri

### exception

Projeye ozel hata siniflarini icerir.

- RentFlowException
- ValidationException
- DuplicateResourceException
- ResourceNotFoundException
- VehicleNotAvailableException
- InvalidVehicleStateException
- InvalidRentalDateException
- ConsoleInputClosedException

## Menu

Uygulama calistiginda asagidaki menu goruntulenir:

```text
==== RentFlow ====
1 - Araclari listele
2 - Arac ekle
3 - Plakaya gore arac ara
4 - Kira ucreti hesapla
5 - Musteriye arac kirala
6 - Arac iade et
7 - Musterileri listele
8 - Musteri ekle
9 - Id'ye gore musteri ara
10 - Kiralamalari listele
11 - Musteriye gore kiralamalari listele
0 - Cikis
```

## Kurulum

Projeyi klonla:

```bash
git clone https://github.com/kullanici-adin/rentflow.git
```

Proje dizinine gir:

```bash
cd rentflow
```

Projeyi test et:

```bash
mvn clean test
```

## Hızlı Başlangıç (Quick Start)

### 1. Build Projesi (One-time)
```bash
mvn clean package -DskipTests
```

### 2. Run Development Mode (H2 In-Memory Database)
```bash
# Windows
run-app.bat dev

# Linux/macOS
./run-app.sh dev

# Manual
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### 3. Run Production Mode (PostgreSQL)
```bash
# Requires: PostgreSQL server running on localhost:5432
# Database: rentflow
# User: rentflow
# Password: Enter at prompt

# Windows
run-app.bat prod

# Linux/macOS
./run-app.sh prod

# Manual
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

### 4. Login Credentials (After Setup)
- **Default Admin**: username: `admin` | password: `admin123`
- **Default User**: username: `user` | password: `user123`

## API Documentation

### Authentication
- **POST** `/auth/login` - Login and get JWT token
- **GET** `/auth/validate` - Validate JWT token

### Vehicles
- **GET** `/vehicles` - List all vehicles
- **GET** `/vehicles/{plate}` - Get vehicle by plate
- **POST** `/vehicles` - Add new vehicle
- **GET** `/vehicles/{plate}/price?days=N` - Calculate rental price
- **PUT** `/vehicles/{plate}/rent` - Mark as rented
- **PUT** `/vehicles/{plate}/return` - Mark as available

### Customers
- **GET** `/customers` - List all customers
- **GET** `/customers/{id}` - Get customer by ID
- **POST** `/customers` - Add new customer

### Rentals
- **GET** `/rentals` - List all rentals
- **GET** `/rentals/{id}` - Get rental by ID
- **GET** `/rentals/customer/{customerId}` - Get rentals by customer
- **POST** `/rentals` - Create new rental
- **PUT** `/rentals/{plate}/return` - Return vehicle

## H2 Console Erişim (Development Only)
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:rentflowdb`
- Username: `sa`
- Password: (leave blank)

**GUI Uygulamasini calistir (JavaFX):**

```bash
mvn javafx:run
```

**Versiyonlar:**

- **v1.0**: Console Application - `mvn clean compile exec:java -Dexec.mainClass=com.rentflow.app.Main`
- **v2.0**: JavaFX GUI - `mvn javafx:run`
- **v3.0**: Spring Boot REST API - `mvn spring-boot:run` (NEW)

Ana siniflar:

- **REST API**: `com.rentflow.api.RentFlowRestApplication`
- **GUI**: `com.rentflow.app.RentFlowApplication`
- **Console** (legacy): `com.rentflow.app.Main`

## Testler

Projede JUnit 5 testleri bulunmaktadir.

Testleri calistirmak icin:

```bash
mvn test
```

Tum testleri temiz build ile calistirmak icin:

```bash
mvn clean test
```

Test kapsami:

- Service-level end-to-end testler
- Console smoke test
- Validation testleri
- Exception davranisi testleri
- Arac kiralama ve iade akisi
- Musteri islemleri
- Fiyat hesaplama
- Hatali input senaryolari

## Ornek Kiralama Akisi

- Musterileri listele
- Araclari listele
- Musteriye arac kirala
- Kiralamalari listele
- Araci iade et
- Kiralamalari tekrar listele

Beklenen durum gecisleri:

VehicleStatus:
`AVAILABLE -> RENTED -> AVAILABLE`

RentalStatus:
`ACTIVE -> COMPLETED`

## Ogrenilen Konular

Bu proje ile pratik edilen Java/OOP konulari:

- Class ve object yapisi
- Encapsulation
- Inheritance
- Polymorphism
- Abstract class
- Enum
- Exception hierarchy
- Repository pattern
- Service layer
- Console controller ayrimi
- Maven
- JUnit 5
- Katmanli mimari
- Refactoring
- Test-driven guvence

## Mevcut Durum

RentFlow **masaüstü uygulaması v4.0** - Production-Ready:

✅ **Completed Sprints:**
- Sprint 1: Console Application (v1.0)
- Sprint 2: JavaFX Desktop GUI (v2.0)
- Sprint 3: Spring Boot REST API (v3.0)
- Sprint 4: Database Integration (JPA/H2)
- Sprint 5: PostgreSQL Production Database with Flyway Migrations
- Sprint 6: JWT Authentication with Login Endpoint
- Sprint 7: Desktop Application Packaging & Deployment Scripts

✅ **Technology Stack:**
- Java 17 (target version)
- Spring Boot 3.2.5
- Spring Data JPA + Hibernate
- PostgreSQL (production) + H2 (development)
- Flyway (database migrations)
- JWT (authentication)
- JavaFX 21 (desktop GUI)
- Maven (build system)
- JUnit 5 (26 test cases, 100% passing)

✅ **Architecture:**
- 3-layer: UI (Console/GUI/API) → Service → Repository
- JPA Entity inheritance (JOINED strategy)
- Exception hierarchy (7 custom exceptions)
- Repository pattern with in-memory + JPA implementations
- Profile-based configuration (dev/prod)
- Role-based security (ADMIN/USER)

✅ **Deployment Ready:**
- Executable JAR packaging
- Windows batch script (run-app.bat)
- Linux/macOS bash script (run-app.sh)
- Configuration management (environment variables, profiles)
- Sample data loading (Flyway V2)
- User authentication table (Flyway V3)

## Gelecek Gelistirmeler (Optional)

Opsiyonel gelistirmeler (if needed):
- Desktop installer (WiX for Windows, .dmg for macOS)
- Application updater mechanism
- Advanced reporting and analytics
- Mobile application (React Native)
- Cloud deployment (Docker, Kubernetes)
- Advanced search filtering
- Custom rental pricing rules
- Late fee penalty automation
- SMS/Email notifications
- Daha detayli kiralama raporlari
- Gec iade cezasi
- Hasar ucreti
- Spring Boot REST API versiyonu
- PostgreSQL entegrasyonu
- Swagger dokumantasyonu

## Lisans

Bu proje egitim ve portfolyo amaciyla gelistirilmistir.
