# RentFlow

RentFlow, Java 17 ile gelistirilmis katmanli mimariye sahip ve **masaüstü uygulaması** olarak tasarlanan bir arac kiralama sistemidir.

Bu proje; Java OOP, service-repository mimarisi, exception yonetimi, Maven, JUnit testleri, Spring Boot REST API, JavaFX GUI ve PostgreSQL database ogrenmek amaciyla gelistirilmistir.

**Sürüm 4.0**: PostgreSQL Production Database ile complete backend + JavaFX GUI

## Ozellikler

- **REST API (Spring Boot)** ✨ NEW
  - Vehicle endpoints
  - Customer endpoints  
  - Rental endpoints
- **GUI Arayüzü (JavaFX)**
  - Tabbed interface
  - Araba ve motosiklet kiralama
  - Müşteri ve araç yönetimi
  - Kiralama işlemleri
- Arac ekleme
  - Car
  - Motorcycle
- Arac listeleme
- Plakaya gore arac arama
- Arac kira ucreti hesaplama
- Musteri ekleme
- Musteri listeleme
- Id'ye gore musteri arama
- Musteriye arac kiralama
- Arac iade etme
- Kiralama kayitlarini listeleme
- Musteriye gore kiralama gecmisi listeleme
- Ozel exception yapisi
- JUnit ile otomatik testler
- Maven build sistemi

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
├── api (NEW - Spring Boot REST)
│   ├── RentFlowRestApplication.java
│   ├── controller/
│   │   ├── VehicleRestController.java
│   │   ├── CustomerRestController.java
│   │   └── RentalRestController.java
│   └── handler/
│       └── GlobalExceptionHandler.java
├── app
│   ├── Main.java (Console - Legacy)
│   ├── RentFlowApplication.java (JavaFX GUI)
│   └── DataInitializer.java
├── service
│   ├── api/ (NEW - Interfaces)
│   │   ├── IVehicleService.java
│   │   ├── ICustomerService.java
│   │   └── IRentalService.java
│   ├── VehicleService.java
│   ├── CustomerService.java
│   └── RentalService.java
├── enums
├── exception
├── model
├── repository
├── ui
│   ├── console (Legacy)
│   │   ├── menu
│   │   ├── controller
│   │   ├── input
│   │   └── printer
│   └── fx (JavaFX GUI)
│       ├── MainWindow.java
│       ├── tab/
│       ├── dialog/
│       └── util/
└── persistence
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

**REST API'yi başlat (Spring Boot):**

**Development (H2 In-Memory Database):**
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
# veya default olarak:
mvn spring-boot:run
```

**Production (PostgreSQL Database):**
```bash
# Önce PostgreSQL'i kuruu ve rentflow database'i olustur:
# createdb -U postgres rentflow

# Sonra application'ı prod profiliyle calistir:
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

Ardindan Postman'da şu endpoint'lere istek gönderebilirsin:
- GET http://localhost:8080/api/vehicles
- GET http://localhost:8080/api/customers
- GET http://localhost:8080/api/rentals
- POST http://localhost:8080/api/vehicles (JSON body ile)

**H2 Console Erişim (Sadece dev profili):**
- http://localhost:8080/h2-console
- JDBC URL: jdbc:h2:mem:rentflowdb
- Username: sa
- Password: (boş)

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

RentFlow su anda masaüstü uygulaması v4.0 seviyesindedir.

Tamamlananlar:

- Katmanli mimari (3-layer architecture)
- Desktop UI (Console + JavaFX GUI)
- Repository-Service ayrimi
- Musteri, arac ve kiralama domain yapisi
- Ozel exception sistemi
- Maven + JUnit test altyapisi
- Basarili build ve test sureci
- CSV ve In-Memory persistence
- Spring Boot REST API (v3.0)
- Global exception handling
- Service interface'leri
- **PostgreSQL + H2 Database Integration (v4.0)**
  - JPA Entity mapping (JOINED inheritance)
  - Flyway database migration
  - Profile-based configuration (dev/prod)
  - Connection pooling (HikariCP)

## Gelecek Gelistirmeler

Planlanan olasi gelistirmeler:

- JWT Authentication
- Desktop Application Packaging (JAR, Windows Installer)
- Application Deployment & Distribution
- Advanced Search ve Filtering
- User Profile Management
- Late Fee Calculation & Reporting
- Daha detayli kiralama raporlari
- Gec iade cezasi
- Hasar ucreti
- Spring Boot REST API versiyonu
- PostgreSQL entegrasyonu
- Swagger dokumantasyonu

## Lisans

Bu proje egitim ve portfolyo amaciyla gelistirilmistir.
