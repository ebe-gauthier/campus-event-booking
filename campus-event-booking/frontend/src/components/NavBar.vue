<template>
  <nav class="navbar">
    <div class="nav-inner container">
      <RouterLink to="/" class="logo">
        <span class="logo-text">Welcome</span>
        <span class="logo-text">Campus-Events</span>
      </RouterLink>

      <div class="nav-links">
        <RouterLink to="/events">Browse Events</RouterLink>
        <RouterLink v-if="auth.isLoggedIn" to="/dashboard">My Bookings</RouterLink>
        <RouterLink v-if="auth.isLoggedIn" to="/admin">Admin</RouterLink>
      </div>

      <div class="nav-auth">
        <template v-if="auth.isLoggedIn">
          <span class="student-name">{{ auth.currentStudent.name }}</span>
          <button class="btn btn-outline" @click="handleLogout">Logout</button>
        </template>
        <template v-else>
          <RouterLink to="/login" class="btn btn-outline">Login</RouterLink>
          <RouterLink to="/register" class="btn btn-primary">Sign Up</RouterLink>
        </template>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { useAuthStore } from '../store/auth.js'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

function handleLogout() {
  auth.logout()
  router.push('/')
}
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0; left: 0; right: 0;
  z-index: 100;
  background: rgba(10,10,15,0.85);
  backdrop-filter: blur(16px);
  border-bottom: 1px solid var(--border);
  height: 64px;
}

.nav-inner {
  display: flex;
  align-items: center;
  height: 100%;
  gap: 32px;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  font-family: 'Syne', sans-serif;
  font-weight: 800;
  font-size: 1.1rem;
  color: var(--text);
}

.logo-icon { font-size: 1.4rem; }

.nav-links {
  display: flex;
  gap: 24px;
  flex: 1;
}

.nav-links a {
  color: var(--text-muted);
  font-size: 0.9rem;
  font-weight: 500;
  transition: color 0.2s;
}

.nav-links a:hover,
.nav-links a.router-link-active {
  color: var(--accent);
}

.nav-auth {
  display: flex;
  align-items: center;
  gap: 12px;
}

.student-name {
  font-size: 0.85rem;
  color: var(--text-muted);
}
</style>
