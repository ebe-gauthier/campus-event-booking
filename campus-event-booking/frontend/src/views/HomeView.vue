<template>
  <div class="container" style="padding-top: 60px; padding-bottom: 60px;">

    <!-- Hero -->
    <div style="text-align: center; margin-bottom: 60px;">
      <h1 style="font-size: 2.5rem; margin-bottom: 12px;">Campus Event Booking</h1>
      <p style="color: var(--text-muted); font-size: 1rem; margin-bottom: 28px;">
        Browse and register for campus events easily.
      </p>
      <div style="display: flex; gap: 12px; justify-content: center;">
        <RouterLink to="/events" class="btn btn-primary">Browse Events</RouterLink>
        <RouterLink v-if="!auth.isLoggedIn" to="/register" class="btn btn-outline">Sign Up</RouterLink>
      </div>
    </div>

    <!-- Upcoming Events -->
    <h2 style="margin-bottom: 20px;">Upcoming Events</h2>
    <div v-if="loading" class="loading"><div class="spinner"></div> Loading...</div>
    <div v-else-if="recentEvents.length === 0" class="empty-state">
      
      <p>No events yet.</p>
    </div>
    <div v-else class="grid-3">
      <EventCard v-for="event in recentEvents" :key="event.id" :event="event" />
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '../store/auth.js'
import { eventApi } from '../api/index.js'
import EventCard from '../components/EventCard.vue'

const auth = useAuthStore()
const recentEvents = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const res = await eventApi.getAll()
    recentEvents.value = res.data.slice(0, 6)
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
})
</script>
