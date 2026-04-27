<template>
  <div class="container" style="padding-top: 40px; padding-bottom: 60px;">

    <h1 class="page-title">My Dashboard</h1>
    <p class="page-subtitle">Welcome, {{ auth.currentStudent?.name }} 👋</p>

    <!-- Student Info -->
    <div class="card" style="padding: 20px; margin-bottom: 32px; display: flex; gap: 24px; align-items: center;">
      <div>
        <div style="font-weight: 700; font-size: 1rem;">{{ auth.currentStudent?.name }}</div>
        <div style="color: var(--text-muted); font-size: 0.875rem;">{{ auth.currentStudent?.email }}</div>
        <div style="color: var(--text-muted); font-size: 0.875rem;">ID: {{ auth.currentStudent?.studentId }}</div>
      </div>
      <div style="margin-left: auto; text-align: center;">
        <div style="font-size: 1.8rem; font-weight: 800; color: var(--accent);">{{ bookings.length }}</div>
        <div style="font-size: 0.8rem; color: var(--text-muted);">Registered Events</div>
      </div>
    </div>

    <!-- Bookings -->
    <h2 style="margin-bottom: 16px;">My Registrations</h2>

    <div v-if="loading" class="loading"><div class="spinner"></div> Loading...</div>

    <div v-else-if="bookings.length === 0" class="empty-state">
      <div class="icon">🎟️</div>
      <p>You haven't registered for any events yet.</p>
      <RouterLink to="/events" class="btn btn-primary" style="margin-top: 16px;">Browse Events</RouterLink>
    </div>

    <div v-else style="display: flex; flex-direction: column; gap: 12px;">
      <div
        v-for="booking in bookings"
        :key="booking.id"
        class="card"
        style="padding: 16px 20px; display: flex; align-items: center; justify-content: space-between;"
      >
        <div>
          <div style="font-weight: 600; margin-bottom: 4px;">{{ booking.eventTitle }}</div>
          <div style="font-size: 0.8rem; color: var(--text-muted);">Booked on {{ formatDate(booking.bookedAt) }}</div>
        </div>
        <div style="display: flex; align-items: center; gap: 12px;">
          <span class="badge" :class="booking.status === 'CONFIRMED' ? 'badge-success' : 'badge-danger'">
            {{ booking.status }}
          </span>
          <RouterLink :to="`/events/${booking.eventId}`" class="btn btn-outline" style="padding: 6px 14px; font-size: 0.85rem;">
            View
          </RouterLink>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '../store/auth.js'
import { bookingApi } from '../api/index.js'

const auth = useAuthStore()
const bookings = ref([])
const loading = ref(true)

function formatDate(d) {
  return new Date(d).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })
}

onMounted(async () => {
  try {
    const res = await bookingApi.getByStudent(auth.currentStudent.id)
    bookings.value = res.data
  } finally {
    loading.value = false
  }
})
</script>
