<template>
  <div class="container" style="padding-top: 40px; padding-bottom: 60px;">
    <h1 class="page-title">All Events</h1>
    <p class="page-subtitle">Browse and register for campus events</p>

    <!-- Filters -->
    <div class="filters">
      <input
        v-model="search"
        @input="handleSearch"
        class="search-input"
        placeholder=" Search events..."
        type="text"
      />
      <div class="category-filters">
        <button
          v-for="cat in ['All', ...categories]"
          :key="cat"
          class="filter-btn"
          :class="{ active: activeCategory === cat }"
          @click="filterByCategory(cat)"
        >
          {{ cat }}
        </button>
      </div>
    </div>

    <!-- Events Grid -->
    <div v-if="loading" class="loading"><div class="spinner"></div> Loading...</div>
    <div v-else-if="events.length === 0" class="empty-state">
      
      <p>No events found. Try a different search.</p>
    </div>
    <div v-else class="grid-3">
      <EventCard v-for="event in events" :key="event.id" :event="event" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { eventApi } from '../api/index.js'
import EventCard from '../components/EventCard.vue'

const route = useRoute()
const events = ref([])
const loading = ref(true)
const search = ref('')
const activeCategory = ref('All')

const categories = ['Sports', 'Music', 'Tech', 'Art', 'Science', 'Career', 'Social', 'Academic']

let searchTimeout = null

async function loadEvents() {
  loading.value = true
  try {
    const res = await eventApi.getAll()
    events.value = res.data
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

async function filterByCategory(cat) {
  activeCategory.value = cat
  loading.value = true
  try {
    if (cat === 'All') {
      const res = await eventApi.getAll()
      events.value = res.data
    } else {
      const res = await eventApi.getByCategory(cat)
      events.value = res.data
    }
  } finally {
    loading.value = false
  }
}

function handleSearch() {
  clearTimeout(searchTimeout)
  searchTimeout = setTimeout(async () => {
    if (!search.value.trim()) {
      loadEvents()
      return
    }
    loading.value = true
    try {
      const res = await eventApi.search(search.value)
      events.value = res.data
    } finally {
      loading.value = false
    }
  }, 400)
}

onMounted(() => {
  if (route.query.category) {
    activeCategory.value = route.query.category
    filterByCategory(route.query.category)
  } else {
    loadEvents()
  }
})
</script>

<style scoped>
.filters {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 32px;
}

.search-input {
  width: 100%;
  max-width: 480px;
  background: var(--surface);
  border: 1.5px solid var(--border);
  color: var(--text);
  padding: 12px 18px;
  border-radius: 10px;
  font-size: 0.95rem;
  transition: border-color 0.2s;
}

.search-input:focus {
  outline: none;
  border-color: var(--accent);
}

.category-filters {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.filter-btn {
  padding: 6px 16px;
  border-radius: 99px;
  font-size: 0.85rem;
  font-weight: 500;
  background: var(--surface);
  border: 1.5px solid var(--border);
  color: var(--text-muted);
  transition: all 0.2s;
  cursor: pointer;
}

.filter-btn:hover { border-color: var(--accent); color: var(--accent); }
.filter-btn.active { background: var(--accent); color: #fff; border-color: var(--accent); }
</style>
