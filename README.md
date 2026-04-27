# 🎓 Campus Event Booking System

A full-stack MVC web application built with **Spring Boot** (backend) + **Vue.js** (frontend) + **MongoDB** (database).

---

##  Project Structure

```
campus-event-booking/
├── backend/          ← Spring Boot (IntelliJ)
│   ├── pom.xml
│   └── src/main/java/com/campus/events/
│       ├── model/          (Event, Student, Booking)
│       ├── repository/     (MongoRepository interfaces)
│       ├── service/        (Business logic)
│       └── controller/     (REST API endpoints)
│
└── frontend/         ← Vue.js (VS Code)
    ├── package.json
    ├── vite.config.js
    └── src/
        ├── api/            (Axios API calls)
        ├── store/          (Pinia auth store)
        ├── router/         (Vue Router)
        ├── views/          (Pages)
        └── components/     (Reusable components)
```

---

##  Setup Instructions

### 1. MongoDB
Make sure MongoDB is running locally:
```bash
mongod
```
The app uses database: `campus_events` on port `27017` (auto-created).

---

### 2. Backend (IntelliJ IDEA)

1. Open the `backend/` folder in IntelliJ
2. Wait for Maven to download dependencies
3. Run `CampusEventsApplication.java`
4. API will be live at: `http://localhost:8080`

> **Note:** Lombok plugin must be installed in IntelliJ. Go to:
> `Settings → Plugins → Search "Lombok" → Install → Enable annotation processing`

---

### 3. Frontend (VS Code)

1. Open the `frontend/` folder in VS Code
2. Install dependencies:
```bash
npm install
```
3. Start dev server:
```bash
npm run dev
```
4. Open: `http://localhost:5173`

---

## REST API Endpoints

### Events
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/events` | Get all events |
| GET | `/api/events/:id` | Get event by ID |
| GET | `/api/events/category/:cat` | Filter by category |
| GET | `/api/events/search?q=` | Search events |
| POST | `/api/events` | Create event |
| PUT | `/api/events/:id` | Update event |
| DELETE | `/api/events/:id` | Delete event |

### Students
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/students/register` | Register student |
| POST | `/api/students/login` | Login |
| GET | `/api/students/:id` | Get student |

### Bookings
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/bookings` | Book an event |
| DELETE | `/api/bookings/cancel` | Cancel booking |
| GET | `/api/bookings/student/:id` | Get student bookings |
| GET | `/api/bookings/event/:id` | Get event bookings |

---

##  MVC Architecture

```
Vue.js (View)
    ↕ HTTP/Axios
Spring Boot Controller (Controller)
    ↕
Spring Service (Business Logic)
    ↕
MongoDB Repository (Model/Data)
    ↕
MongoDB Database
```

---

##  Features

- ✅ Student registration & login
- ✅ Browse all events with search & category filter
- ✅ Event detail page with booking
- ✅ Cancel registrations
- ✅ Student dashboard showing bookings
- ✅ Admin panel to create/edit/delete events
- ✅ Real-time spot availability tracking
- ✅ CORS configured between frontend & backend

---

##  screenshoots

## Frontend
<img width="1820" height="913" alt="Screenshot 2026-04-27 113356" src="https://github.com/user-attachments/assets/c023c04e-3f82-41ae-a384-7ebe34116085" />

<img width="1882" height="941" alt="Screenshot 2026-04-27 113001" src="https://github.com/user-attachments/assets/908550dc-4e1b-4b6d-9d54-b7684d4b6115" />


<img width="1918" height="966" alt="Screenshot 2026-04-27 113454" src="https://github.com/user-attachments/assets/b9d6bf74-42a7-4c55-a742-46a63c849bb0" />

<img width="1886" height="935" alt="Screenshot 2026-04-27 113800" src="https://github.com/user-attachments/assets/27c0eccd-422e-41bf-88d1-bf279af41193" />


## backend
<img width="1801" height="833" alt="Screenshot 2026-04-27 114126" src="https://github.com/user-attachments/assets/2531bbe4-0a93-4a62-a8a7-34d8bdfd1c1a" />

<img width="1918" height="959" alt="Screenshot 2026-04-27 114234" src="https://github.com/user-attachments/assets/c9da7d3a-2d1b-4f79-ba0b-3fcc1058e871" />

<img width="1919" height="959" alt="Screenshot 2026-04-27 114307" src="https://github.com/user-attachments/assets/b0aedb16-7077-42c1-8ef4-406871015813" />

<img width="1914" height="971" alt="Screenshot 2026-04-27 114350" src="https://github.com/user-attachments/assets/24b59d00-ceee-4965-afb4-cb8ea62d48ea" />


## DATA
<img width="1907" height="880" alt="Screenshot 2026-04-27 114501" src="https://github.com/user-attachments/assets/62300ffd-c3e0-4289-902b-5db61ccb7056" />

