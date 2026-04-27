<template>
  <div class="container" style="padding-top: 40px; padding-bottom: 60px; max-width: 800px;">
    <RouterLink to="/events" class="back-link">← Back to Events</RouterLink>

    <div v-if="loading" class="loading"><div class="spinner"></div> Loading...</div>

    <div v-else-if="event" class="event-detail card">
      <div class="event-hero">
        <div class="event-category-badge">
          <span class="cat-icon">{{ categoryIcon }}</span>
          <span class="badge badge-accent">{{ event.category }}</span>
        </div>
        <h1 class="event-title">{{ event.title }}</h1>
        <div class="event-organizer">Organized by <strong>{{ event.organizer || 'Campus Team' }}</strong></div>
      </div>

      <div class="event-content">
        <div class="info-grid">
          <div class="info-item">
            <span class="info-icon">Date</span>
            <div>
              <div class="info-label">Date & Time</div>
              <div class="info-value">{{ formatDate(event.date) }}</div>
            </div>
          </div>
          <div class="info-item">
            <span class="info-icon">Location</span>
            <div>
              <div class="info-label">Location</div>
              <div class="info-value">{{ event.location }}</div>
            </div>
          </div>
          
            <div>
              <div class="info-label">Capacity</div>
              <div class="info-value">{{ event.capacity }} total · <span :class="spotsClass">{{ availableSpots }} available</span></div>
            </div>
          </div>
        

        <div class="description">
          <h3>About this Event</h3>
          <p>{{ event.description }}</p>
        </div>

        <!-- Booking Section -->
        <div class="booking-section">
          <template v-if="!auth.isLoggedIn">
            <p class="text-muted">Please <RouterLink to="/login" style="color:var(--accent)">log in</RouterLink> to register for this event.</p>
          </template>
          <template v-else-if="isAlreadyBooked">
            <div class="booked-badge">You're registered for this event!</div>
            <button class="btn btn-danger" @click="handleCancel" :disabled="cancelling">
              {{ cancelling ? 'Cancelling...' : 'Cancel Registration' }}
            </button>
          </template>
          <template v-else-if="availableSpots === 0">
            <div class="full-badge"> This event is full</div>
          </template>
          <template v-else>
            <button class="btn btn-primary" style="font-size:1rem; padding:14px 32px;" @click="handleBook" :disabled="booking">
              {{ booking ? 'Registering...' : ' Register for Event' }}
            </button>
          </template>
          <div v-if="message" :class="['msg', msgType]">{{ message }}</div>
        </div>
      </div>
    </div>

    <div v-else class="empty-state">
      
      <p>Event not found.</p>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useAuthStore } from '../store/auth.js'
import { eventApi, bookingApi } from '../api/index.js'

const route = useRoute()
const auth = useAuthStore()
const event = ref(null)
const loading = ref(true)
const booking = ref(false)
const cancelling = ref(false)
const message = ref('')
const msgType = ref('success-msg')
const myBookings = ref([])

const categoryIcons = {
  'Sports': '⚽', 'Music': '🎵', 'Tech': '💻', 'Art': '🎨',
  'Science': '🔬', 'Career': '💼', 'Social': '🎉', 'Academic': '📚'
}

const categoryIcon = computed(() => categoryIcons[event.value?.category] || 'category')

const availableSpots = computed(() =>
  (event.value?.capacity || 0) - (event.value?.registeredStudentIds?.length || 0)
)

const spotsClass = computed(() => {
  if (availableSpots.value === 0) return 'spots-full'
  if (availableSpots.value < 5) return 'spots-low'
  return 'spots-ok'
})

const isAlreadyBooked = computed(() => {
  if (!auth.currentStudent) return false
  return myBookings.value.some(b => b.eventId === event.value?.id && b.status === 'CONFIRMED')
})

function formatDate(d) {
  return new Date(d).toLocaleString('en-US', {
    weekday: 'long', month: 'long', day: 'numeric', year: 'numeric', hour: '2-digit', minute: '2-digit'
  })
}

async function handleBook() {
  booking.value = true
  message.value = ''
  try {
    await bookingApi.book(auth.currentStudent.id, event.value.id)
    message.value = 'Successfully registered! 🎉'
    msgType.value = 'success-msg'
    await loadData()
  } catch (e) {
    message.value = e.response?.data?.error || 'Failed to register'
    msgType.value = 'error-msg'
  } finally {
    booking.value = false
  }
}

async function handleCancel() {
  cancelling.value = true
  message.value = ''
  try {
    await bookingApi.cancel(auth.currentStudent.id, event.value.id)
    message.value = 'Registration cancelled.'
    msgType.value = 'success-msg'
    await loadData()
  } catch (e) {
    message.value = e.response?.data?.error || 'Failed to cancel'
    msgType.value = 'error-msg'
  } finally {
    cancelling.value = false
  }
}

async function loadData() {
  const [eventRes] = await Promise.all([
    eventApi.getById(route.params.id)
  ])
  event.value = eventRes.data
  if (auth.currentStudent) {
    const bookRes = await bookingApi.getByStudent(auth.currentStudent.id)
    myBookings.value = bookRes.data
  }
}

onMounted(async () => {
  try {
    await loadData()
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.back-link {
  display: inline-block;
  color: var(--text-muted);
  font-size: 0.9rem;
  margin-bottom: 24px;
  transition: color 0.2s;
}
.back-link:hover { color: var(--accent); }

.event-detail { padding: 0; }

.event-hero {
  padding: 36px 36px 28px;
  border-bottom: 1px solid var(--border);
  background: linear-gradient(135deg, rgba(108,99,255,0.06), transparent);
}

.event-category-badge {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 16px;
}

.cat-icon { font-size: 2rem; }

.event-title {
  font-size: 2rem;
  font-weight: 800;
  margin-bottom: 8px;
}

.event-organizer {
  font-size: 0.9rem;
  color: var(--text-muted);
}

.event-content {
  padding: 32px 36px;
  display: flex;
  flex-direction: column;
  gap: 28px;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 16px;
}

.info-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 16px;
  background: var(--surface-2);
  border-radius: 10px;
}

.info-icon { font-size: 1.4rem; }
.info-label { font-size: 0.75rem; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.05em; margin-bottom: 4px; }
.info-value { font-size: 0.9rem; font-weight: 500; }

.spots-ok { color: var(--success); }
.spots-low { color: var(--warning); }
.spots-full { color: var(--danger); }

.description h3 { margin-bottom: 10px; font-size: 1rem; }
.description p { color: var(--text-muted); line-height: 1.7; }

.booking-section {
  padding: 24px;
  background: var(--surface-2);
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.booked-badge {
  font-size: 0.95rem;
  font-weight: 600;
  color: var(--success);
}

.full-badge {
  font-size: 0.95rem;
  font-weight: 600;
  color: var(--danger);
}

.msg { margin-top: 8px; }
</style>
