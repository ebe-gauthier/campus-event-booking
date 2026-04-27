<template>
  <RouterLink :to="`/events/${event.id}`" class="event-card">
    <div class="event-header">
      <div class="category-icon">{{ categoryIcon }}</div>
      <span class="badge badge-accent">{{ event.category }}</span>
    </div>
    <div class="event-body">
      <h3 class="event-title">{{ event.title }}</h3>
      <p class="event-desc">{{ event.description?.slice(0, 100) }}...</p>
    </div>
    <div class="event-footer">
      <div class="event-meta">
        <span>📍 {{ event.location }}</span>
        <span>📅 {{ formatDate(event.date) }}</span>
      </div>
      <div class="event-spots" :class="{ 'low': availableSpots < 5, 'full': availableSpots === 0 }">
        <span v-if="availableSpots > 0">{{ availableSpots }} spots left</span>
        <span v-else>Full</span>
      </div>
    </div>
  </RouterLink>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({ event: Object })

const categoryIcons = {
  'Sports': '⚽', 'Music': '🎵', 'Tech': '💻', 'Art': '🎨',
  'Science': '🔬', 'Career': '💼', 'Social': '🎉', 'Academic': '📚'
}

const categoryIcon = computed(() =>
  categoryIcons[props.event.category] || '📌'
)

const availableSpots = computed(() =>
  props.event.capacity - (props.event.registeredStudentIds?.length || 0)
)

function formatDate(dateStr) {
  return new Date(dateStr).toLocaleDateString('en-US', {
    month: 'short', day: 'numeric', year: 'numeric'
  })
}
</script>

<style scoped>
.event-card {
  display: flex;
  flex-direction: column;
  gap: 14px;
  padding: 22px;
  background: var(--surface);
  border: 1px solid var(--border);
  border-radius: var(--radius-lg);
  transition: all 0.25s ease;
  cursor: pointer;
}

.event-card:hover {
  border-color: var(--accent);
  transform: translateY(-3px);
  box-shadow: 0 12px 40px rgba(108,99,255,0.15);
}

.event-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.category-icon { font-size: 1.8rem; }

.event-title {
  font-size: 1.1rem;
  font-weight: 700;
  margin-bottom: 6px;
  color: var(--text);
}

.event-desc {
  font-size: 0.875rem;
  color: var(--text-muted);
  line-height: 1.5;
}

.event-footer {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-top: auto;
}

.event-meta {
  display: flex;
  flex-direction: column;
  gap: 4px;
  font-size: 0.8rem;
  color: var(--text-muted);
}

.event-spots {
  font-size: 0.8rem;
  font-weight: 600;
  color: var(--success);
}
.event-spots.low { color: var(--warning); }
.event-spots.full { color: var(--danger); }
</style>
