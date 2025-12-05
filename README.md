# To-Do List App (JavaFX + MySQL)

#### Description:
The **To-Do List Management System** is a desktop-based application developed using **Java**, **JavaFX**, and **MySQL** with full **JDBC integration**. This project is designed to help users efficiently manage daily tasks through a secure, user-friendly, and modern graphical interface. The system allows users to create accounts, log in securely, add tasks, edit tasks, delete tasks, and archive completed tasks for better organization and productivity.

This project was developed as part of an **Object-Oriented Programming (OOP)** course and demonstrates the practical implementation of core OOP principles along with real-time database connectivity. The application is fully modular, scalable, and built with clean separation between the user interface, business logic, and database access layers.

---

## 🎯 Objectives

- To develop a **user-friendly task management system** using Java and MySQL.
- To implement core **Object-Oriented Programming (OOP)** principles in a real-world application.
- To integrate **JDBC (Java Database Connectivity)** for persistent data storage.
- To manage application data using a structured **DAO (Data Access Object)** pattern.
- To design a **modular, maintainable, and scalable** desktop application.

---

## 🛠️ Technologies Used

- **Java (JDK 8+)**
- **JavaFX** – for graphical user interface
- **MySQL** – for database storage
- **JDBC** – for database connectivity
- **FXML** – for UI design
- **Scene Builder** – for UI layout design
- **MySQL Workbench** – for database management

---

## 📦 Features

- ✅ User Signup & Secure Login  
- ✅ Add New Tasks  
- ✅ Edit Existing Tasks  
- ✅ Delete Tasks  
- ✅ Archive Completed Tasks  
- ✅ Restore Archived Tasks  
- ✅ User-Based Data Isolation  
- ✅ Persistent Storage with MySQL  
- ✅ Clean Dark-Themed Interface  
- ✅ Real-Time UI & Database Synchronization  

---

## 🧠 OOP Principles Applied

### 1. Encapsulation
All class data members (e.g., in `User`, `TodoTask`) are **private** and accessed using **getters and setters**, ensuring data security and integrity.

### 2. Inheritance
Common database operation logic is reused across DAO classes such as `TaskDAO` and `ArchiveTaskDAO`.

### 3. Polymorphism
Methods like `displayTask()` and DAO operations behave differently depending on whether the task is active or archived.

### 4. Abstraction
- `DBConnection` class abstracts database connectivity.
- DAO interfaces abstract database operations from the rest of the system.

---

## 🔁 System Workflow

1. **User Registration / Login**
   - Users create an account or log in securely.
   - Credentials are stored in the `users` table.

2. **Main Dashboard**
   - Displays all active tasks for the logged-in user.

3. **Add Task**
   - Users input title, description, date, and time.
   - Data is saved permanently in MySQL.

4. **Edit Task**
   - Existing tasks can be modified in real time.

5. **Archive Task**
   - Completed tasks are moved to the `archived_tasks` table.

6. **Restore Task**
   - Archived tasks can be moved back to the active task list.

All actions update both the **database and UI instantly**.

---

## 🧪 Testing & Results

- ✅ User accounts are properly stored and validated.
- ✅ Tasks persist after application restart.
- ✅ Archive and restore features work correctly.
- ✅ Database operations validated using MySQL Workbench.
- ✅ JavaFX UI provides a smooth and modern experience.

---

## 🗄️ Database Design

**Tables Used:**
- `users` – Stores user credentials
- `tasks` – Stores active tasks
- `archived_tasks` – Stores completed tasks

Each task is strictly linked to its respective user for full data isolation and security.

---

## 🚀 Future Enhancements

- ⏰ Task reminder and notification system
- 📊 Task priority sorting & filtering
- ☁️ Cloud synchronization for multi-device access
- 📱 Mobile version using Android
- 🔐 Two-factor authentication (2FA)

---

## ✅ Conclusion

The **To-Do List Management System** successfully demonstrates the integration of **Java OOP principles with real-time database connectivity using JDBC**. The system is modular, secure, efficient, and user-friendly. This project fulfills all requirements of a complex computing system by combining structured programming, database integration, and event-driven UI design.

---

## 👨‍💻 Authors

- **Muzammil**
- **Waryam**
- **Safiullah**

---

## 📌 Repository

If you are using this project for learning or academic purposes, feel free to fork and modify it.

---

© 2025 – Java To-Do List Management System

