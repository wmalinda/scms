# 🎓 Smart Campus Management System (SCMS)

A Java-based **Smart Campus Management System** designed to support campus operations such as **room booking**, **maintenance request handling**, and **user notification management**.  
The project is developed using **Object-Oriented Programming (OOP)** concepts and key **software design patterns**.


---


## 📌 Table of Contents
- [Project Overview](#-project-overview)
- [Key Features](#-key-features)
- [System Users](#-system-users)
- [Technologies Used](#-technologies-used)
- [Project Structure](#-project-structure)
- [System Design](#-system-design)
  - [Use Case Diagram](#-use-case-diagram)
  - [UML Class Diagram](#-uml-class-diagram)
- [Screenshots](#-screenshots)
- [How to Run](#-how-to-run)
- [Design Patterns Used](#-design-patterns-used)
- [Future Enhancements](#-future-enhancements)
- [Author](#-author)
- [License](#-license)


---


## 📖 Project Overview

The **Smart Campus Management System (SCMS)** is a software solution that manages campus facilities and resources.  
It allows users to **book rooms**, submit **maintenance requests**, and receive **notifications** regarding system events.

This system supports multiple user roles such as:
- Students
- Staff Members
- Administrators

The project also demonstrates practical implementation of design patterns such as:
- Facade
- Factory
- Strategy
- Observer
- Adapter


---


## ⭐ Key Features

### ✅ Room Management
- Add and manage campus rooms
- Categorize rooms by type (lab, lecture hall, etc.)
- Room creation is handled through a Factory Pattern

### ✅ Booking Management
- Book rooms based on availability
- Prevent booking conflicts
- Booking validation is implemented using Strategy Pattern

### ✅ Maintenance Request Handling
- Submit maintenance requests for rooms/facilities
- Track urgency and status
- Admin/Staff can update maintenance progress

### ✅ Notification System
- Users receive notifications when system events occur
- Observer Pattern supports notification subscriptions
- Adapter Pattern formats notifications

### ✅ Error Handling
Custom exception handling improves system reliability.


---


## 👥 System Users

| Role | Description |
|------|-------------|
| Student | Can book rooms and submit requests |
| Staff Member | Can book rooms and manage maintenance |
| Administrator | Has full access and management privileges |


---


## 🛠 Technologies Used

- **Java**
- **Maven**
- **OOP Concepts**
- **UML Modeling**
- **Design Patterns**
- **GitHub Version Control**


---


## 📂 Project Structure

scms/
│
├── pom.xml
├── README.md
├── .gitignore
│
└── src/
├── main/
│ ├── java/
│ │ ├── app/
│ │ │ └── ScmsConsoleApp.java
│ │ │
│ │ ├── facade/
│ │ │ └── CampusFacade.java
│ │ │
│ │ ├── repository/
│ │ │ └── CampusRepository.java
│ │ │
│ │ ├── model/
│ │ │ ├── User.java
│ │ │ ├── Student.java
│ │ │ ├── StaffMember.java
│ │ │ ├── Administrator.java
│ │ │ ├── Room.java
│ │ │ ├── Booking.java
│ │ │ ├── MaintenanceRequest.java
│ │ │ ├── AppNotification.java
│ │ │ └── enums/
│ │ │ ├── UserRole.java
│ │ │ ├── RoomCategory.java
│ │ │ ├── MaintenanceStatus.java
│ │ │ └── MaintenanceUrgency.java
│ │ │
│ │ ├── service/
│ │ │ ├── BookingService.java
│ │ │ ├── MaintenanceService.java
│ │ │ └── NotificationService.java
│ │ │
│ │ ├── patterns/
│ │ │ ├── factory/
│ │ │ │ └── RoomFactory.java
│ │ │ │
│ │ │ ├── strategy/
│ │ │ │ ├── BookingValidationStrategy.java
│ │ │ │ └── DefaultBookingValidationStrategy.java
│ │ │ │
│ │ │ ├── observer/
│ │ │ │ └── NotificationObserver.java
│ │ │ │
│ │ │ └── adapter/
│ │ │ └── NotificationContentAdapter.java
│ │ │
│ │ └── exception/
│ │ ├── ScmsSystemException.java
│ │ ├── DuplicateDataException.java
│ │ ├── InvalidBookingException.java
│ │ └── UnauthorizedActionException.java
│ │
│ └── resources/
│
└── test/
└── java/ 


---


## 🏗 System Design

This project is designed using a layered architecture:

- **UI Layer:** Console application (`ScmsConsoleApp`)
- **Facade Layer:** Centralized controller (`CampusFacade`)
- **Service Layer:** Business logic (Booking, Maintenance, Notification)
- **Repository Layer:** Data storage (`CampusRepository`)
- **Model Layer:** Core domain entities (User, Room, Booking, etc.)


---


## 📌 Use Case Diagram

The system supports the following main use cases:

### Student Use Cases
- Book Room
- View Available Rooms
- Submit Maintenance Request
- Receive Notifications

### Staff Use Cases
- Book Room
- Submit Maintenance Request
- Update Maintenance Status
- Receive Notifications

### Administrator Use Cases
- Manage Rooms
- Manage Users
- View Bookings
- Approve/Reject Maintenance Requests
- Send Notifications

📍 **Use Case Diagram Link**  
👉 `docs/usecase-diagram.png`


---


## 📌 UML Class Diagram

The UML class diagram shows the relationships between:

- Users (Student, StaffMember, Administrator)
- Room, Booking, MaintenanceRequest
- Services and Repository
- Implemented Design Patterns

📍 **UML Class Diagram Link**  
👉 `docs/uml-class-diagram.png`

📍 **Mermaid UML Code Link**  
👉 `docs/uml-class-diagram.md`


---


## 🖼 Screenshots

Add screenshots here after running the system:

| Feature | Screenshot |
|---------|------------|
| Main Menu | `docs/screenshots/main-menu.png` |
| Room Booking | `docs/screenshots/booking.png` |
| Maintenance Request | `docs/screenshots/maintenance.png` |
| Notifications | `docs/screenshots/notifications.png` |


---


## ⚙️ How to Run

### ✅ Requirements
- Java JDK 8 or above
- Maven installed

### ✅ Clone Repository
```bash
git clone https://github.com/wmalinda/scms.git

Navigate into Project 
cd scms

Build the Project 
mvn clean install

Run the Application
If configured with Maven exec plugin:
mvn exec:java

Or run manually through IDE:
Open project in IntelliJ / Eclipse
Run ScmsConsoleApp.java


---


🧩 Design Patterns Used
✅ Facade Pattern
CampusFacade provides a simplified interface for system operations.
✅ Factory Pattern
RoomFactory handles room object creation.
✅ Strategy Pattern
BookingValidationStrategy provides flexible booking validation rules.
✅ Observer Pattern
NotificationObserver enables notification subscriptions.
✅ Adapter Pattern
NotificationContentAdapter standardizes notification message formatting.


---


🚀 Future Enhancements
Database integration (MySQL/PostgreSQL)
GUI/Web Application (Spring Boot)
Authentication & Login system
Advanced room scheduling and calendar view
Report generation for admin
Unit testing with JUnit
Email/SMS notification integration


👨‍💻 Author
Developed by wmalinda
Project: Smart Campus Management System (SCMS)


📜 License
This project is developed for educational purposes and is free to use and modify.