<template>
  <div class="container" style="padding-top: 40px; padding-bottom: 60px;">
    <div class="admin-header">
      <div>
        <h1 class="page-title">Admin Panel</h1>
        <p class="page-subtitle">Manage campus events</p>
      </div>
      <button class="btn btn-primary" @click="showForm = !showForm">
        {{ showForm ? '✕ Cancel' : '+ New Event' }}
      </button>
    </div>

    <!-- Event Form -->
    <div v-if="showForm" class="event-form card">
      <h3>{{ editingId ? 'Edit Event' : 'Create New Event' }}</h3>
      <form @submit.prevent="handleSubmit" class="form-grid">
        <div class="form-group">
          <label>Title</label>
          <input v-model="form.title" type="text" placeholder="Event title" required />
        </div>
        <div class="form-group">
          <label>Category</label>
          <select v-model="form.category" required>
            <option value="">Select Category</option>
            <option v-for="c in categories" :key="c" :value="c">{{ c }}</option>
          </select>
        </div>
        <div class="form-group full-span">
          <label>Description</label>
          <textarea v-model="form.description" rows="3" placeholder="Describe the event..." required></textarea>
        </div>
        <div class="form-group">
          <label>Location</label>
          <input v-model="form.location" type="text" placeholder="Hall A, Building 2..." required />
        </div>
        <div class="form-group">
          <label>Date & Time</label>
          <input v-model="form.date" type="datetime-local" required />
        </div>
        <div class="form-group">
          <label>Capacity</label>
          <input v-model.number="form.capacity" type="number" min="1" placeholder="100" required />
        </div>
        <div class="form-group">
          <label>Organizer</label>
          <input v-model="form.organizer" type="text" placeholder="Dept. of CS..." />
        </div>

        <div v-if="formError" class="error-msg full-span">{{ formError }}</div>
        <div v-if="formSuccess" class="success-msg full-span">{{ formSuccess }}</div>

        <div class="form-actions full-span">
          <button type="submit" class="btn btn-primary" :disabled="submitting">
            {{ submitting ? 'Saving...' : (editingId ? 'Update Event' : 'Create Event') }}
          </button>
          <button type="button" class="btn btn-outline" @click="resetForm">Reset</button>
        </div>
      </form>
    </div>

    <!-- Events Table -->
    <div class="events-table card">
      <div class="table-header">
        <h3>All Events ({{ events.length }})</h3>
      </div>
      <div v-if="loading" class="loading"><div class="spinner"></div> Loading...</div>
      <table v-else class="table">
        <thead>
          <tr>
            <th>Event</th>
            <th>Category</th>
            <th>Date</th>
            <th>Location</th>
            <th>Spots</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="events.length === 0">
            <td colspan="6" style="text-align:center; color: var(--text-muted); padding: 40px;">
              No events yet. Create one above!
            </td>
          </tr>
          <tr v-for="event in events" :key="event.id">
            <td><strong>{{ event.title }}</strong></td>
            <td><span class="badge badge-accent">{{ event.category }}</span></td>
            <td>{{ formatDate(event.date) }}</td>
            <td>{{ event.location }}</td>
            <td>
              <span :class="getSpotsClass(event)">
                {{ event.capacity - (event.registeredStudentIds?.length || 0) }} / {{ event.capacity }}
              </span>
            </td>
            <td>
              <div class="row-actions">
                <button class="btn btn-outline" style="padding:5px 12px; font-size:0.8rem;" @click="startEdit(event)">Edit</button>
                <button class="btn btn-danger" style="padding:5px 12px; font-size:0.8rem;" @click="deleteEvent(event.id)">Delete</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { eventApi } from '../api/index.js'

const events = ref([])
const loading = ref(true)
const showForm = ref(false)
const editingId = ref(null)
const submitting = ref(false)
const formError = ref('')
const formSuccess = ref('')

const categories = ['Sports', 'Music', 'Tech', 'Art', 'Science', 'Career', 'Social', 'Academic']

const defaultForm = () => ({
  title: '', description: '', location: '',
  date: '', category: '', capacity: 50, organizer: ''
})

const form = ref(defaultForm())

function formatDate(d) {
  return new Date(d).toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })
}

function getSpotsClass(event) {
  const spots = event.capacity - (event.registeredStudentIds?.length || 0)
  if (spots === 0) return 'spots-full'
  if (spots < 5) return 'spots-low'
  return 'spots-ok'
}

function startEdit(event) {
  editingId.value = event.id
  form.value = {
    title: event.title,
    description: event.description,
    location: event.location,
    date: event.date?.slice(0, 16),
    category: event.category,
    capacity: event.capacity,
    organizer: event.organizer || ''
  }
  showForm.value = true
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

function resetForm() {
  form.value = defaultForm()
  editingId.value = null
  formError.value = ''
  formSuccess.value = ''
}

async function handleSubmit() {
  submitting.value = true
  formError.value = ''
  formSuccess.value = ''
  try {
    const payload = { ...form.value, date: new Date(form.value.date).toISOString() }
    if (editingId.value) {
      await eventApi.update(editingId.value, payload)
      formSuccess.value = 'Event updated successfully!'
    } else {
      await eventApi.create(payload)
      formSuccess.value = 'Event created successfully!'
    }
    resetForm()
    showForm.value = false
    await loadEvents()
  } catch (e) {
    formError.value = e.response?.data?.message || 'Failed to save event.'
  } finally {
    submitting.value = false
  }
}

async function deleteEvent(id) {
  if (!confirm('Delete this event?')) return
  await eventApi.delete(id)
  await loadEvents()
}

async function loadEvents() {
  loading.value = true
  try {
    const res = await eventApi.getAll()
    events.value = res.data
  } finally {
    loading.value = false
  }
}

onMounted(loadEvents)
</script>

<style scoped>
.admin-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24px;
}

.event-form {
  padding: 28px;
  margin-bottom: 24px;
}

.event-form h3 { margin-bottom: 20px; }

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.full-span { grid-column: 1 / -1; }

.form-actions { display: flex; gap: 12px; }

textarea {
  resize: vertical;
  min-height: 80px;
}

.events-table { overflow: hidden; }

.table-header {
  padding: 20px 24px;
  border-bottom: 1px solid var(--border);
}

.table {
  width: 100%;
  border-collapse: collapse;
}

.table th {
  padding: 12px 16px;
  text-align: left;
  font-size: 0.78rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
  color: var(--text-muted);
  border-bottom: 1px solid var(--border);
}

.table td {
  padding: 14px 16px;
  font-size: 0.9rem;
  border-bottom: 1px solid var(--border);
  color: var(--text);
}

.table tr:last-child td { border-bottom: none; }
.table tr:hover td { background: rgba(255,255,255,0.02); }

.row-actions { display: flex; gap: 8px; }

.spots-ok { color: var(--success); font-weight: 600; }
.spots-low { color: var(--warning); font-weight: 600; }
.spots-full { color: var(--danger); font-weight: 600; }
</style>
