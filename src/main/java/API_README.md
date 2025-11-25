# IMS API - Frontend Developer Documentation

**Version:** 1.0
**Base URL:** `http://localhost:8080`
**Authentication:** HTTP Basic Auth
**CORS Allowed Origin:** `http://localhost:4200`

---

## Table of Contents

1. [Overview](#overview)
2. [Authentication & Authorization](#authentication--authorization)
3. [API Response Format](#api-response-format)
4. [Error Handling](#error-handling)
5. [Endpoints](#endpoints)
   - [Auth Endpoints](#auth-endpoints)
   - [User Endpoints](#user-endpoints)
   - [Role Endpoints](#role-endpoints)
   - [Post Endpoints](#post-endpoints)
   - [Comment Endpoints](#comment-endpoints)
6. [Pagination & Filtering](#pagination--filtering)
7. [CORS Configuration](#cors-configuration)
8. [Environment Variables](#environment-variables)
9. [OpenAPI / Swagger Documentation](#openapi--swagger-documentation)
10. [Generating TypeScript Client](#generating-typescript-client)
11. [Questions for Backend Team](#questions-for-backend-team)

---

## Overview

The IMS (Issue Management System) API is a Spring Boot REST API that manages posts (issues/complaints/announcements), comments, users, and roles. It uses:

- **Authentication:** HTTP Basic Auth (username:password encoded in Authorization header)
- **Database:** H2 in-memory database (development mode)
- **CORS:** Configured to allow `http://localhost:4200` (Angular frontend)
- **Response Format:** Standardized JSON wrapper with `code`, `status`, `message`, `data`, and `timestamp`

---

## Authentication & Authorization

### Authentication Method

**HTTP Basic Authentication** is used for all protected endpoints.

- **Header:** `Authorization: Basic <base64(username:password)>`
- **Example:** For user `admin` with password `admin123`:
  ```
  Authorization: Basic YWRtaW46YWRtaW4xMjM=
  ```

### Public Endpoints

- `POST /auth/register` - Register a new user
- `POST /auth/login` - Validate credentials (returns user object)

### Protected Endpoints

All endpoints under `/api/**` require authentication:

- **Admin Only:**
  - `GET /api/users` - List all users
  - `GET /api/roles` - List all roles
  - `POST /api/roles` - Create a role

- **Authenticated Users:**
  - All `/api/posts/**` endpoints
  - All `/api/comments/**` endpoints

### Roles

- `ROLE_USER` - Standard user
- `ROLE_ADMIN` - Administrator with full access

---

## API Response Format

### Success Response

All successful API responses (except 204 No Content) follow this structure:

```json
{
  "code": 200,
  "status": "OK",
  "message": "Operation successful message",
  "data": { /* response payload */ },
  "timestamp": "2025-11-25T10:30:45.123456"
}
```

**Fields:**
- `code` (number): HTTP status code
- `status` (string): HTTP status reason phrase
- `message` (string): Human-readable success message
- `data` (object|array|null): Response payload (optional)
- `timestamp` (string): ISO 8601 timestamp (LocalDateTime)

### Example Success Response

```json
{
  "code": 200,
  "status": "OK",
  "message": "Post retrieved successfully",
  "data": {
    "id": 1,
    "title": "Server Down",
    "content": "The production server is not responding",
    "type": "ISSUE",
    "status": "DRAFT",
    "createdByUsername": "john.doe",
    "createdById": 5,
    "createdAt": "2025-11-20T14:30:00",
    "updatedAt": "2025-11-20T14:30:00",
    "submittedAt": "2025-11-20T14:30:00",
    "resolvedAt": null
  },
  "timestamp": "2025-11-25T10:30:45.123456"
}
```

---

## Error Handling

### Error Response Format

Error responses vary based on the exception type:

**Generic Error (500):**
```json
"class java.lang.IllegalArgumentException: Post not found"
```

**Data Integrity Violation (409):**
```json
"Data Integrity Violation could not execute statement..."
```

**Authentication Error (401):**
```json
{
  "error": "Unauthorized",
  "message": "Authentication failed",
  "timestamp": "2025-11-25T10:30:45"
}
```

**Access Denied (403):**
```json
{
  "error": "Forbidden",
  "message": "Access denied",
  "timestamp": "2025-11-25T10:30:45"
}
```

### Common HTTP Status Codes

- `200 OK` - Successful GET, PUT, PATCH
- `201 Created` - Successful POST
- `204 No Content` - Successful DELETE
- `400 Bad Request` - Validation error
- `401 Unauthorized` - Missing or invalid credentials
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `409 Conflict` - Data integrity violation (duplicate username, etc.)
- `500 Internal Server Error` - Server error

---

## Endpoints

### Auth Endpoints

#### Register User

**`POST /auth/register`**

Create a new user account.

**Request Body:**
```json
{
  "username": "john.doe",
  "password": "password123",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": 1234567890,
  "roles": [
    {
      "id": 1,
      "roleName": "ROLE_USER"
    }
  ]
}
```

**Response (201 Created):**
```json
{
  "id": 5,
  "username": "john.doe",
  "password": "$2a$10$...",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": 1234567890,
  "roles": [
    {
      "id": 1,
      "roleName": "ROLE_USER"
    }
  ]
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "password": "password123",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNumber": 1234567890,
    "roles": [{"id": 1, "roleName": "ROLE_USER"}]
  }'
```

---

#### Login

**`POST /auth/login`**

Validate user credentials.

**Request Body:**
```json
{
  "username": "john.doe",
  "password": "password123"
}
```

**Response (200 OK):**
```json
{
  "id": 5,
  "username": "john.doe",
  "password": "$2a$10$...",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phoneNumber": 1234567890,
  "roles": [
    {
      "id": 1,
      "roleName": "ROLE_USER"
    }
  ]
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe",
    "password": "password123"
  }'
```

**Notes:**
- This endpoint validates credentials and returns the user object.
- For subsequent requests, use HTTP Basic Auth header with username:password.
- Store credentials securely in the frontend (consider using session storage for Basic Auth).

---

### User Endpoints

#### Get All Users

**`GET /api/users`**

**Authorization:** Admin only (`ROLE_ADMIN`)

**Response (201 Created):** *(Note: Backend returns 201, should be 200)*
```json
[
  {
    "id": 1,
    "username": "admin",
    "password": "$2a$10$...",
    "firstName": "Admin",
    "lastName": "User",
    "email": "admin@example.com",
    "phoneNumber": 9876543210,
    "roles": [
      {
        "id": 2,
        "roleName": "ROLE_ADMIN"
      }
    ]
  },
  {
    "id": 5,
    "username": "john.doe",
    "password": "$2a$10$...",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "phoneNumber": 1234567890,
    "roles": [
      {
        "id": 1,
        "roleName": "ROLE_USER"
      }
    ]
  }
]
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/users \
  -u admin:admin123
```

---

### Role Endpoints

#### Get All Roles

**`GET /api/roles`**

**Authorization:** Admin only (`ROLE_ADMIN`)

**Request Body:** *(Note: GET with body is unusual, may be a backend bug)*
```json
{}
```

**Response (201 Created):** *(Note: Backend returns 201, should be 200)*
```json
[
  {
    "id": 1,
    "roleName": "ROLE_USER"
  },
  {
    "id": 2,
    "roleName": "ROLE_ADMIN"
  }
]
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/roles \
  -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{}'
```

---

#### Create Role

**`POST /api/roles`**

**Authorization:** Admin only (`ROLE_ADMIN`)

**Request Body:**
```json
{
  "roleName": "ROLE_MODERATOR"
}
```

**Response (201 Created):**
```json
"created"
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/roles \
  -u admin:admin123 \
  -H "Content-Type: application/json" \
  -d '{
    "roleName": "ROLE_MODERATOR"
  }'
```

---

### Post Endpoints

#### Create Post

**`POST /api/posts`**

**Authorization:** Required (authenticated user)

**Request Body:**
```json
{
  "title": "Server Down",
  "content": "The production server is not responding since 2 PM",
  "type": "ISSUE",
  "createdById": 5
}
```

**Request Fields:**
- `title` (string, required, max 255 chars): Post title
- `content` (string, required): Post content/description
- `type` (enum, required): Post type - `ISSUE`, `COMPLAINT`, `ANNOUNCEMENT`, `GENERAL`
- `createdById` (number, required, positive): ID of the user creating the post

**Response (201 Created):**
```json
{
  "code": 201,
  "status": "Created",
  "message": "Post created successfully",
  "data": {
    "id": 1,
    "title": "Server Down",
    "content": "The production server is not responding since 2 PM",
    "type": "ISSUE",
    "status": "DRAFT",
    "createdByUsername": "john.doe",
    "createdById": 5,
    "createdAt": "2025-11-25T14:30:00",
    "updatedAt": "2025-11-25T14:30:00",
    "submittedAt": "2025-11-25T14:30:00",
    "resolvedAt": null
  },
  "timestamp": "2025-11-25T14:30:00.123456"
}
```

**Post Status Values:**
- `DRAFT` - Initial state
- `PENDING_APPROVAL` - Awaiting approval
- `APPROVED` - Approved
- `REJECTED` - Rejected
- `RESOLVED` - Resolved

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/posts \
  -u john.doe:password123 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Server Down",
    "content": "The production server is not responding since 2 PM",
    "type": "ISSUE",
    "createdById": 5
  }'
```

---

#### Get All Posts (with Pagination & Filtering)

**`GET /api/posts`**

**Authorization:** Required (authenticated user)

**Query Parameters:**
- `q` (string, optional): Free-text search in title and content
- `status` (enum, optional): Filter by status - `DRAFT`, `PENDING_APPROVAL`, `APPROVED`, `REJECTED`, `RESOLVED`
- `type` (enum, optional): Filter by type - `ISSUE`, `COMPLAINT`, `ANNOUNCEMENT`, `GENERAL`
- `createdById` (number, optional): Filter by creator user ID
- `page` (number, default: `0`): Page number (zero-based)
- `size` (number, default: `10`): Page size
- `sort` (string, default: `createdAt`): Sort field - `createdAt`, `updatedAt`, `title`, `submittedAt`
- `dir` (string, default: `desc`): Sort direction - `asc`, `desc`

**Example URL:**
```
GET /api/posts?q=server&status=DRAFT&type=ISSUE&page=0&size=10&sort=createdAt&dir=desc
```

**Response (200 OK):**
```json
{
  "code": 200,
  "status": "OK",
  "message": "Posts retrieved successfully",
  "data": {
    "content": [
      {
        "id": 1,
        "title": "Server Down",
        "content": "The production server is not responding since 2 PM",
        "type": "ISSUE",
        "status": "DRAFT",
        "createdByUsername": "john.doe",
        "createdById": 5,
        "createdAt": "2025-11-25T14:30:00",
        "updatedAt": "2025-11-25T14:30:00",
        "submittedAt": "2025-11-25T14:30:00",
        "resolvedAt": null
      },
      {
        "id": 2,
        "title": "Database Backup Issue",
        "content": "Daily backup failed last night",
        "type": "ISSUE",
        "status": "DRAFT",
        "createdByUsername": "jane.smith",
        "createdById": 6,
        "createdAt": "2025-11-24T10:15:00",
        "updatedAt": "2025-11-24T10:15:00",
        "submittedAt": "2025-11-24T10:15:00",
        "resolvedAt": null
      }
    ],
    "pagination": {
      "currentPage": 0,
      "totalPages": 5,
      "totalItems": 47,
      "pageSize": 10,
      "hasNext": true,
      "hasPrevious": false
    }
  },
  "timestamp": "2025-11-25T14:30:00.123456"
}
```

**cURL Example:**
```bash
curl -X GET 'http://localhost:8080/api/posts?q=server&status=DRAFT&page=0&size=10&sort=createdAt&dir=desc' \
  -u john.doe:password123
```

---

#### Get Post by ID

**`GET /api/posts/{id}`**

**Authorization:** Required (authenticated user)

**Path Parameters:**
- `id` (number): Post ID

**Response (200 OK):**
```json
{
  "code": 200,
  "status": "OK",
  "message": "Post retrieved successfully",
  "data": {
    "id": 1,
    "title": "Server Down",
    "content": "The production server is not responding since 2 PM",
    "type": "ISSUE",
    "status": "DRAFT",
    "createdByUsername": "john.doe",
    "createdById": 5,
    "createdAt": "2025-11-25T14:30:00",
    "updatedAt": "2025-11-25T14:30:00",
    "submittedAt": "2025-11-25T14:30:00",
    "resolvedAt": null
  },
  "timestamp": "2025-11-25T14:30:00.123456"
}
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/posts/1 \
  -u john.doe:password123
```

---

#### Update Post

**`PUT /api/posts/{id}`**

**Authorization:** Required (authenticated user)

**Path Parameters:**
- `id` (number): Post ID

**Request Body:**
```json
{
  "title": "Server Down - URGENT",
  "content": "The production server is not responding since 2 PM. Multiple users affected.",
  "type": "ISSUE",
  "createdById": 5
}
```

**Response (200 OK):**
```json
{
  "code": 200,
  "status": "OK",
  "message": "Post updated successfully",
  "data": {
    "id": 1,
    "title": "Server Down - URGENT",
    "content": "The production server is not responding since 2 PM. Multiple users affected.",
    "type": "ISSUE",
    "status": "DRAFT",
    "createdByUsername": "john.doe",
    "createdById": 5,
    "createdAt": "2025-11-25T14:30:00",
    "updatedAt": "2025-11-25T15:45:00",
    "submittedAt": "2025-11-25T14:30:00",
    "resolvedAt": null
  },
  "timestamp": "2025-11-25T15:45:00.123456"
}
```

**cURL Example:**
```bash
curl -X PUT http://localhost:8080/api/posts/1 \
  -u john.doe:password123 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Server Down - URGENT",
    "content": "The production server is not responding since 2 PM. Multiple users affected.",
    "type": "ISSUE",
    "createdById": 5
  }'
```

---

#### Update Post Status

**`PATCH /api/posts/{id}/status`**

**Authorization:** Required (authenticated user)

**Path Parameters:**
- `id` (number): Post ID

**Request Body:**
```json
{
  "status": "RESOLVED"
}
```

**Status Values:**
- `DRAFT`
- `PENDING_APPROVAL`
- `APPROVED`
- `REJECTED`
- `RESOLVED`

**Response (200 OK):**
```json
{
  "code": 200,
  "status": "OK",
  "message": "Post status updated successfully",
  "data": {
    "id": 1,
    "title": "Server Down",
    "content": "The production server is not responding since 2 PM",
    "type": "ISSUE",
    "status": "RESOLVED",
    "createdByUsername": "john.doe",
    "createdById": 5,
    "createdAt": "2025-11-25T14:30:00",
    "updatedAt": "2025-11-25T16:00:00",
    "submittedAt": "2025-11-25T14:30:00",
    "resolvedAt": "2025-11-25T16:00:00"
  },
  "timestamp": "2025-11-25T16:00:00.123456"
}
```

**Note:** When status is set to `RESOLVED`, the `resolvedAt` timestamp is automatically set.

**cURL Example:**
```bash
curl -X PATCH http://localhost:8080/api/posts/1/status \
  -u john.doe:password123 \
  -H "Content-Type: application/json" \
  -d '{
    "status": "RESOLVED"
  }'
```

---

### Comment Endpoints

#### Create Comment

**`POST /api/posts/{postId}/comments`**

**Authorization:** Required (authenticated user, uses current user from security context)

**Path Parameters:**
- `postId` (number): Post ID to comment on

**Request Body:**
```json
{
  "content": "I am also experiencing this issue. The server went down around 2:15 PM."
}
```

**Response (201 Created):**
```json
{
  "code": 201,
  "status": "Created",
  "message": "Comment created successfully",
  "data": {
    "id": 1,
    "content": "I am also experiencing this issue. The server went down around 2:15 PM.",
    "postId": 1,
    "createdById": 6,
    "createdByUsername": "jane.smith",
    "createdAt": "2025-11-25T14:45:00",
    "updatedAt": "2025-11-25T14:45:00"
  },
  "timestamp": "2025-11-25T14:45:00.123456"
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/posts/1/comments \
  -u jane.smith:password456 \
  -H "Content-Type: application/json" \
  -d '{
    "content": "I am also experiencing this issue. The server went down around 2:15 PM."
  }'
```

---

#### Get Comments by Post

**`GET /api/posts/{postId}/comments`**

**Authorization:** Required (authenticated user)

**Path Parameters:**
- `postId` (number): Post ID

**Response (200 OK):**
```json
{
  "code": 200,
  "status": "OK",
  "message": "Comments retrieved successfully",
  "data": [
    {
      "id": 1,
      "content": "I am also experiencing this issue. The server went down around 2:15 PM.",
      "postId": 1,
      "createdById": 6,
      "createdByUsername": "jane.smith",
      "createdAt": "2025-11-25T14:45:00",
      "updatedAt": "2025-11-25T14:45:00"
    },
    {
      "id": 2,
      "content": "We are investigating the issue.",
      "postId": 1,
      "createdById": 1,
      "createdByUsername": "admin",
      "createdAt": "2025-11-25T15:00:00",
      "updatedAt": "2025-11-25T15:00:00"
    }
  ],
  "timestamp": "2025-11-25T15:30:00.123456"
}
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/posts/1/comments \
  -u john.doe:password123
```

---

#### Get Comment by ID

**`GET /api/comments/{id}`**

**Authorization:** Required (authenticated user)

**Path Parameters:**
- `id` (number): Comment ID

**Response (200 OK):**
```json
{
  "code": 200,
  "status": "OK",
  "message": "Comment retrieved successfully",
  "data": {
    "id": 1,
    "content": "I am also experiencing this issue. The server went down around 2:15 PM.",
    "postId": 1,
    "createdById": 6,
    "createdByUsername": "jane.smith",
    "createdAt": "2025-11-25T14:45:00",
    "updatedAt": "2025-11-25T14:45:00"
  },
  "timestamp": "2025-11-25T15:30:00.123456"
}
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/comments/1 \
  -u john.doe:password123
```

---

#### Update Comment

**`PUT /api/comments/{id}`**

**Authorization:** Required (comment owner or admin)

**Path Parameters:**
- `id` (number): Comment ID

**Request Body:**
```json
{
  "content": "I am also experiencing this issue. The server went down around 2:15 PM. UPDATE: It's back online now."
}
```

**Response (200 OK):**
```json
{
  "code": 200,
  "status": "OK",
  "message": "Comment updated successfully",
  "data": {
    "id": 1,
    "content": "I am also experiencing this issue. The server went down around 2:15 PM. UPDATE: It's back online now.",
    "postId": 1,
    "createdById": 6,
    "createdByUsername": "jane.smith",
    "createdAt": "2025-11-25T14:45:00",
    "updatedAt": "2025-11-25T16:30:00"
  },
  "timestamp": "2025-11-25T16:30:00.123456"
}
```

**Authorization Rules:**
- Comment owner can update their own comments
- Admins (`ROLE_ADMIN`) can update any comment

**cURL Example:**
```bash
curl -X PUT http://localhost:8080/api/comments/1 \
  -u jane.smith:password456 \
  -H "Content-Type: application/json" \
  -d '{
    "content": "I am also experiencing this issue. The server went down around 2:15 PM. UPDATE: It'\''s back online now."
  }'
```

---

#### Delete Comment

**`DELETE /api/comments/{id}`**

**Authorization:** Required (comment owner or admin)

**Path Parameters:**
- `id` (number): Comment ID

**Response (204 No Content):**
```
(empty body)
```

**Authorization Rules:**
- Comment owner can delete their own comments
- Admins (`ROLE_ADMIN`) can delete any comment

**cURL Example:**
```bash
curl -X DELETE http://localhost:8080/api/comments/1 \
  -u jane.smith:password456
```

---

## Pagination & Filtering

### Pagination

The `GET /api/posts` endpoint supports pagination via query parameters:

**Parameters:**
- `page` (number, default: `0`): Zero-based page index
- `size` (number, default: `10`): Number of items per page
- `sort` (string, default: `createdAt`): Field to sort by (`createdAt`, `updatedAt`, `title`, `submittedAt`)
- `dir` (string, default: `desc`): Sort direction (`asc`, `desc`)

**Example:**
```
GET /api/posts?page=2&size=20&sort=title&dir=asc
```

### Pagination Metadata

Responses include pagination metadata:

```json
{
  "pagination": {
    "currentPage": 0,
    "totalPages": 5,
    "totalItems": 47,
    "pageSize": 10,
    "hasNext": true,
    "hasPrevious": false
  }
}
```

**Fields:**
- `currentPage`: Current page number (zero-based)
- `totalPages`: Total number of pages
- `totalItems`: Total number of items across all pages
- `pageSize`: Number of items per page
- `hasNext`: Whether there is a next page
- `hasPrevious`: Whether there is a previous page

### Filtering

The `GET /api/posts` endpoint supports filtering via query parameters:

**Parameters:**
- `q` (string, optional): Free-text search in title and content (case-insensitive)
- `status` (enum, optional): Filter by post status
  - Values: `DRAFT`, `PENDING_APPROVAL`, `APPROVED`, `REJECTED`, `RESOLVED`
- `type` (enum, optional): Filter by post type
  - Values: `ISSUE`, `COMPLAINT`, `ANNOUNCEMENT`, `GENERAL`
- `createdById` (number, optional): Filter by creator user ID

**Example:**
```
GET /api/posts?q=server&status=PENDING_APPROVAL&type=ISSUE&createdById=5
```

**Note:** All filters are applied with AND logic (all conditions must match).

---

## CORS Configuration

The backend is configured to allow CORS requests from the Angular frontend.

**Allowed Origin:** `http://localhost:4200`
**Allowed Methods:** `GET`, `POST`, `PUT`, `DELETE`
**Allowed Headers:** `*` (all headers)
**Credentials:** `true` (allows cookies and auth headers)

**Source:** `com.apsharma.ims_api.security.CorsConfig`

### Frontend Proxy Configuration (Recommended)

For development, use Angular's proxy to avoid CORS issues:

**`proxy.conf.json`** (see Frontend Developer Checklist)

---

## Environment Variables

### Backend Configuration

**`application.properties`:**
```properties
spring.application.name=ims-api
spring.datasource.url=jdbc:h2:~/test
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
```

**Default Port:** `8080`

### Frontend Environment Variables

Create environment files for Angular:

**`src/environments/environment.ts`** (development):
```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080',
  apiBasePath: '/api'
};
```

**`src/environments/environment.prod.ts`** (production):
```typescript
export const environment = {
  production: true,
  apiUrl: 'https://api.yourdomain.com',
  apiBasePath: '/api'
};
```

---

## OpenAPI / Swagger Documentation

### Adding OpenAPI Support (Backend)

The backend does not currently expose OpenAPI/Swagger documentation. To enable it:

1. **Add springdoc-openapi dependency to `pom.xml`:**

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

2. **Rebuild the project:**
```powershell
.\mvnw clean install
```

3. **Start the application and access Swagger UI:**
```
http://localhost:8080/swagger-ui.html
```

4. **Access OpenAPI JSON spec:**
```
http://localhost:8080/v3/api-docs
```

### Exporting OpenAPI Spec

Once OpenAPI is enabled, export the spec:

**PowerShell:**
```powershell
Invoke-WebRequest -Uri "http://localhost:8080/v3/api-docs" -OutFile "openapi.json"
```

**cURL:**
```bash
curl http://localhost:8080/v3/api-docs -o openapi.json
```

---

## Generating TypeScript Client

### Prerequisites

1. **Node.js and npm** installed
2. **OpenAPI Generator CLI** installed globally

### Install OpenAPI Generator CLI

**PowerShell:**
```powershell
npm install -g @openapitools/openapi-generator-cli
```

### Generate TypeScript Angular Client

**Step 1: Export OpenAPI Spec**
```powershell
# Start the backend first, then:
Invoke-WebRequest -Uri "http://localhost:8080/v3/api-docs" -OutFile "openapi.json"
```

**Step 2: Generate Client**
```powershell
openapi-generator-cli generate `
  -i openapi.json `
  -g typescript-angular `
  -o ./src/app/generated-api `
  --additional-properties=ngVersion=17,providedInRoot=true,withInterfaces=true
```

**Parameters:**
- `-i openapi.json`: Input OpenAPI spec file
- `-g typescript-angular`: Generator type (Angular TypeScript)
- `-o ./src/app/generated-api`: Output directory
- `--additional-properties`:
  - `ngVersion=17`: Angular version (use 18 or 19 for latest)
  - `providedInRoot=true`: Provide services at root level
  - `withInterfaces=true`: Generate TypeScript interfaces

**Step 3: Import in Angular Module**

The generated services are automatically provided at root level due to `providedInRoot=true`.

**Usage Example:**
```typescript
import { PostControllerService } from './generated-api/api/postController.service';
import { PostResponse } from './generated-api/model/postResponse';

constructor(private postService: PostControllerService) {}

ngOnInit() {
  this.postService.getAllPosts(undefined, undefined, undefined, undefined, 0, 10)
    .subscribe((response: any) => {
      console.log(response.data.content);
    });
}
```

---

## Angular Project Setup Commands

### Step 1: Create Angular Project

**PowerShell:**
```powershell
# Install Angular CLI globally (if not installed)
npm install -g @angular/cli

# Create new Angular project (latest version)
ng new ims-frontend --routing --style=scss --strict

# Navigate to project
cd ims-frontend
```

**Options:**
- `--routing`: Generate routing module
- `--style=scss`: Use SCSS for styling
- `--strict`: Enable strict type checking

### Step 2: Install Recommended Dependencies

**PowerShell:**
```powershell
# Angular Material (optional, for UI components)
ng add @angular/material

# OpenAPI Generator CLI
npm install -g @openapitools/openapi-generator-cli

# HTTP Client (already included in Angular)
# RxJS (already included in Angular)

# Development dependencies
npm install --save-dev @types/node
```

### Step 3: Generate API Client

**PowerShell:**
```powershell
# Create directory for OpenAPI spec
New-Item -ItemType Directory -Force -Path ./api-specs

# Export OpenAPI spec from backend (backend must be running)
Invoke-WebRequest -Uri "http://localhost:8080/v3/api-docs" -OutFile "./api-specs/openapi.json"

# Generate TypeScript Angular client
openapi-generator-cli generate `
  -i ./api-specs/openapi.json `
  -g typescript-angular `
  -o ./src/app/generated-api `
  --additional-properties=ngVersion=18,providedInRoot=true,withInterfaces=true
```

### Step 4: Configure Proxy

Create `proxy.conf.json` in project root:

**PowerShell:**
```powershell
@"
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true
  },
  "/auth": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true
  }
}
"@ | Out-File -FilePath proxy.conf.json -Encoding utf8
```

Update `angular.json` to use proxy:

```json
"serve": {
  "builder": "@angular-devkit/build-angular:dev-server",
  "options": {
    "proxyConfig": "proxy.conf.json"
  }
}
```

### Step 5: Run Angular Dev Server

**PowerShell:**
```powershell
ng serve --open
```

The application will open at `http://localhost:4200`.

---

## Complete PowerShell Setup Script

Copy and run this complete setup script:

```powershell
# IMS Frontend Setup Script

# Step 1: Install Angular CLI
Write-Host "Installing Angular CLI..." -ForegroundColor Green
npm install -g @angular/cli

# Step 2: Create Angular project
Write-Host "Creating Angular project..." -ForegroundColor Green
ng new ims-frontend --routing --style=scss --strict --skip-git

# Step 3: Navigate to project
cd ims-frontend

# Step 4: Install Angular Material
Write-Host "Installing Angular Material..." -ForegroundColor Green
ng add @angular/material --skip-confirmation

# Step 5: Install OpenAPI Generator CLI
Write-Host "Installing OpenAPI Generator CLI..." -ForegroundColor Green
npm install -g @openapitools/openapi-generator-cli

# Step 6: Install dev dependencies
Write-Host "Installing development dependencies..." -ForegroundColor Green
npm install --save-dev @types/node

# Step 7: Create API specs directory
Write-Host "Creating API specs directory..." -ForegroundColor Green
New-Item -ItemType Directory -Force -Path ./api-specs

# Step 8: Create proxy configuration
Write-Host "Creating proxy configuration..." -ForegroundColor Green
@"
{
  "/api": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true,
    "logLevel": "debug"
  },
  "/auth": {
    "target": "http://localhost:8080",
    "secure": false,
    "changeOrigin": true,
    "logLevel": "debug"
  }
}
"@ | Out-File -FilePath proxy.conf.json -Encoding utf8

Write-Host "Setup complete!" -ForegroundColor Green
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Yellow
Write-Host "1. Start the backend API (http://localhost:8080)" -ForegroundColor Yellow
Write-Host "2. Export OpenAPI spec:" -ForegroundColor Yellow
Write-Host "   Invoke-WebRequest -Uri 'http://localhost:8080/v3/api-docs' -OutFile './api-specs/openapi.json'" -ForegroundColor Cyan
Write-Host "3. Generate TypeScript client:" -ForegroundColor Yellow
Write-Host "   openapi-generator-cli generate -i ./api-specs/openapi.json -g typescript-angular -o ./src/app/generated-api --additional-properties=ngVersion=18,providedInRoot=true,withInterfaces=true" -ForegroundColor Cyan
Write-Host "4. Update angular.json to add proxy config to serve options" -ForegroundColor Yellow
Write-Host "5. Run development server: ng serve --open" -ForegroundColor Yellow
```

---

## Questions for Backend Team

### Critical Questions

1. **OpenAPI/Swagger Documentation:**
   - Can you add `springdoc-openapi` dependency to expose OpenAPI spec?
   - This is essential for generating the TypeScript client.

2. **API Response Consistency:**
   - Some endpoints return different status codes than expected:
     - `GET /api/users` returns `201 Created` instead of `200 OK`
     - `GET /api/roles` returns `201 Created` instead of `200 OK`
   - Can these be corrected?

3. **Role Endpoints:**
   - `GET /api/roles` requires a request body (unusual for GET). Is this intentional?
   - Should this be changed to not require a body?

4. **Authentication Response:**
   - Should the `/auth/login` endpoint return a token instead of the full user object?
   - Should passwords be excluded from response objects?

5. **Error Response Format:**
   - Can error responses be standardized to use `ApiResponseBuilder` format?
   - Current error messages are inconsistent (plain strings vs JSON objects).

6. **Validation Errors:**
   - How should validation errors (400 Bad Request) be formatted?
   - Can we get field-level error details for form validation?

7. **Refresh Token / Logout:**
   - Are refresh token or logout endpoints planned?
   - Current Basic Auth doesn't require these, but confirm for future JWT migration.

8. **Post/Comment Ownership:**
   - Are there specific authorization rules for updating/deleting posts?
   - Currently only comments enforce ownership checks.

### Nice-to-Have Questions

1. **API Versioning:**
   - Is API versioning planned (e.g., `/api/v1/posts`)?

2. **Rate Limiting:**
   - Are there any rate limits on API endpoints?

3. **Pagination Defaults:**
   - Can pagination defaults be configured (current: page=0, size=10)?

4. **File Upload:**
   - Will file upload be needed for posts or comments (attachments)?

5. **WebSocket Support:**
   - Will real-time updates be needed (e.g., for comments or status changes)?

6. **Search Improvements:**
   - Should the `q` parameter support advanced search (AND/OR logic, field-specific search)?

7. **Audit Trail:**
   - Should the API expose edit history or audit logs for posts/comments?

8. **Notification System:**
   - Will there be notification endpoints (e.g., user mentions, status changes)?

---

## Summary

This README provides all the information needed for frontend developers to:

1. ✅ Understand authentication (Basic Auth with username:password)
2. ✅ Know all available endpoints and their parameters
3. ✅ See request/response examples with actual JSON
4. ✅ Use curl commands to test endpoints
5. ✅ Understand pagination and filtering
6. ✅ Configure CORS and proxy for development
7. ✅ Set up Angular project with exact PowerShell commands
8. ✅ Generate TypeScript API client (once OpenAPI is enabled)
9. ✅ Know what questions to ask the backend team

**Next Step:** Backend team should add `springdoc-openapi` dependency to enable OpenAPI spec generation, then frontend can generate the TypeScript client and begin development.

---

**Document Version:** 1.0
**Last Updated:** 2025-11-25
**Maintained By:** Backend Team
**For Questions:** Contact backend team or refer to source code at `C:\Users\apars\OneDrive\Documents\ims-api`

