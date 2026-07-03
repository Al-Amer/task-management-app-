# 📋 Task Management App

A simple desktop application to manage your daily tasks, built with **JavaFX** and **Spring Boot**.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.x-green)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue)
![SQLite](https://img.shields.io/badge/SQLite-3.44.1-lightgrey)

---

## ✨ Features

- ➕ Add tasks with title, description, and creator name
- ✅ Mark tasks as done/undone (double-click or use button)
- 🟢 Completed tasks turn green automatically
- 🗑️ Delete tasks you no longer need
- 👤 Filter tasks by creator name
- 📊 View statistics (total, done, pending)
- 💾 All data saved in SQLite database

---

## 🛠️ Technologies Used

| Technology | Purpose |
|------------|---------|
| **Java 17** | Programming language |
| **Spring Boot** | Backend framework (DI, Data management) |
| **JavaFX** | Desktop GUI |
| **Spring Data JPA** | Database operations |
| **SQLite** | Embedded database |
| **Maven** | Build tool |

---

## 📁 Project Structure
task-management-app/
├── src/main/java/com/example/taskmanagement/
│ ├── TaskManagementAppApplication.java # Main entry point
│ ├── TaskManagementAppUI.java # JavaFX GUI
│ ├── Task.java # Entity/model
│ ├── TaskRepository.java # Database operations
│ └── TaskService.java # Business logic
├── src/main/resources/
│ └── application.properties # Configuration
├── pom.xml # Dependencies
└── task.db # SQLite database



---

## 🚀 How to Run

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Steps

1. ## Clone the repository**
   ```bash
   git clone https://github.com/yourusername/task-management-app.git
   cd task-management-app

2. ## Build the project
 ```bash
  mvn clean install
```
4. ## Run the application
```bash
  mvn javafx:run
```

## Or run TaskManagementAppApplication.main() from your IDE.
