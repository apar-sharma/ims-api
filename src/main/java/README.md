# 📮 Issue Tracking / Post Management System

A full-stack application that allows users to submit posts (issues,
complaints, announcements, lost & found, help requests), which must be
reviewed and approved by admins before being visible.

This system is designed with a clean workflow, role-based access, and a
modern tech stack using Spring Boot(3.5.8), jdk 21 for the backend and Angular(latest version) for the
frontend.

## 🚀 Features

### 👤 User Features

-   Register and log in
-   Create posts of different types:
    -   Issue (broken furniture, damaged equipment)
    -   Complaint (unhygienic food, bad smell)
    -   Announcement (events, notices)
    -   Other general posts (lost & found, blood requests)
-   View:
    -   Their own posts
    -   All approved posts
-   Add comments to posts
-   Track status of posts through their lifecycle

### 🛠️ Admin Features

-   View all posts (pending, approved, rejected, closed)
-   Approve or reject posts
-   Assign updates or modify post details
-   Add comments
-   Manage workflow transitions

## 📌 Post Status Lifecycle

DRAFT → PENDING_APPROVAL → APPROVED → RESOLVED\
↘\
REJECTED

## 🔐 Roles & Permissions

| Action                          | USER | ADMIN |
|----------------------------------|:----:|:-----:|
| Create Post                      | ✔️   | ✔️    |
| Submit Post for Approval         | ✔️   | -     |
| Approve / Reject Post            | -    | ✔️    |
| Assign / Update Post             | -    | ✔️    |
| Add Comment                      | ✔️   | ✔️    |
| View Posts (own + approved)      | ✔️   | ✔️*   |


> Admin can view **all** posts in the system.

## 🧱 System Architecture

### Controller Layer

-   REST APIs for authentication, posts, and comments

### Service Layer

-   Business logic
-   Validation
-   Workflow handling

### Repository Layer

-   JPA repositories

### Security Layer

-   Basic Auth (default)
-   Optional JWT
-   Role-based authorization

## 🛡️ Security Flow

### Basic Authentication (default)

-   Username/password stored in DB
-   HTTP basic headers

### JWT Authentication (Optional)

-   Stateless security
-   Token-based authentication

### Endpoint Rules

  | Endpoint     |  Access   | 
  |--------------|:---------:|
  | `/auth/**`   |  Public   |
  | `/api/**`    | Protected |  




