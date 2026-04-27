<template>
  <div class="auth-page">
    <div class="auth-card card">
      <div class="auth-header">
        
        <h1>Create Account</h1>
        <p>Join your campus event community</p>
      </div>

      <form class="auth-form" @submit.prevent="handleRegister">
        <div class="form-row">
          <div class="form-group">
            <label>Full Name</label>
            <input v-model="form.name" type="text" placeholder="John Doe" required />
          </div>
          <div class="form-group">
            <label>Student ID</label>
            <input v-model="form.studentId" type="text" placeholder="STU-12345" required />
          </div>
        </div>
        <div class="form-group">
          <label>Email Address</label>
          <input v-model="form.email" type="email" placeholder="you@university.edu" required />
        </div>
        <div class="form-group">
          <label>Department</label>
          <select v-model="form.department">
            <option value="">Select Department</option>
            <option v-for="d in departments" :key="d" :value="d">{{ d }}</option>
          </select>
        </div>
        <div class="form-group">
          <label>Password</label>
          <input v-model="form.password" type="password" placeholder="Min. 6 characters" required />
        </div>

        <div v-if="error" class="error-msg">{{ error }}</div>

        <button type="submit" class="btn btn-primary full-width" :disabled="loading">
          {{ loading ? 'Creating account...' : 'Create Account' }}
        </button>
      </form>

      <div class="auth-footer">
        Already have an account?
        <RouterLink to="/login" style="color: var(--accent)">Sign in</RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../store/auth.js'

const router = useRouter()
const auth = useAuthStore()
const loading = ref(false)
const error = ref('')

const form = ref({ name: '', studentId: '', email: '', department: '', password: '' })

const departments = [
  'Computer Science', 'Engineering', 'Business', 'Medicine',
  'Law', 'Arts', 'Science', 'Education', 'Social Sciences'
]

async function handleRegister() {
  loading.value = true
  error.value = ''
  try {
    await auth.register(form.value)
    router.push('/dashboard')
  } catch (e) {
    error.value = e.response?.data?.error || 'Registration failed. Please try again.'
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-page {
  min-height: calc(100vh - 64px);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 24px;
}

.auth-card {
  width: 100%;
  max-width: 500px;
  padding: 40px;
}

.auth-header {
  text-align: center;
  margin-bottom: 32px;
}

.auth-icon { font-size: 2.5rem; margin-bottom: 12px; }
.auth-header h1 { font-size: 1.8rem; margin-bottom: 6px; }
.auth-header p { color: var(--text-muted); font-size: 0.9rem; }

.auth-form { display: flex; flex-direction: column; gap: 16px; }

.form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }

.full-width { width: 100%; justify-content: center; padding: 12px; font-size: 1rem; }

.auth-footer { margin-top: 24px; text-align: center; font-size: 0.9rem; color: var(--text-muted); }
</style>
