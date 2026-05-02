# Modern Todo App

A full-stack todo application with a Spring Boot backend and an Angular frontend. It lets you create tasks, mark them complete, delete them, and keep everything synced through a REST API.

## How It Works

The app is split into two parts:

- Backend exposes REST endpoints at `http://localhost:8080/api/tasks`
- Frontend uses Angular `HttpClient` to call those endpoints and render the UI

Task flow:

1. The Angular app loads task data from the backend
2. New tasks are submitted from the form
3. The backend assigns a UUID to each task on add
4. Toggle and delete actions update the saved task list
5. The updated list is returned back to the UI

## Unique Storage Idea

This project uses a simple persistence approach.

Each todo item is represented by a `Task` object with `title`, `description`, `durationMinutes`, and `completed` attributes. The backend stores the tasks as a `List<Task>`, and a `TaskStore` JPA entity keeps that entire list as a JSON string.

Jackson `ObjectMapper` serializes the list before saving and deserializes it back into Java objects when reading. In practice, the app keeps all tasks in a single database row.

That makes the data model easy to reason about for a small todo app, while still using JPA and a real database table.

## Tech Stack

### Backend

- Java 17
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Jackson Databind
- H2 database for local development
- Maven Wrapper (`mvnw`)

### Frontend

- Angular 21 standalone components
- TypeScript
- RxJS
- Angular HttpClient
- Tailwind CSS utility classes
- FormsModule for the task form

## Features

- Add a new task with title, description, and duration
- View tasks in glassmorphism cards
- Toggle a task complete/incomplete
- Delete a task
- Dark gradient background UI
- Cross-origin communication between frontend and backend

## Installation

### Prerequisites

- Java 17
- Node.js 20+ recommended
- npm
- Git

### Clone the repo

```powershell
git clone https://github.com/RAR2025/Modern-Todo-App.git
cd modern_todo_app
```

## Backend Setup

```powershell
cd backend
./mvnw clean spring-boot:run
```

If you want to build first:

```powershell
cd backend
./mvnw clean package
```

## Frontend Setup

```powershell
cd frontend
npm install
npm start
```

To build the Angular app:

```powershell
cd frontend
npm run build
```

## API Endpoints

- `GET /api/tasks/` - Fetch all tasks
- `POST /api/tasks/` - Add a task
- `DELETE /api/tasks/{id}` - Delete a task
- `PATCH /api/tasks/{id}/toggle` - Toggle completion status

## Notes

- The backend uses H2 by default for local development
- The app is configured for CORS so the Angular frontend can talk to the Spring Boot API
- Task IDs are generated automatically with UUID on create

## Made With Love

Made with ❤️ by Ruturaj Rajwade
