import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { studentApi } from '../api/index.js'

export const useAuthStore = defineStore('auth', () => {
  const student = ref(JSON.parse(localStorage.getItem('student') || 'null'))

  const isLoggedIn = computed(() => !!student.value)
  const currentStudent = computed(() => student.value)

  async function login(email, password) {
    const res = await studentApi.login({ email, password })
    student.value = res.data
    localStorage.setItem('student', JSON.stringify(res.data))
    return res.data
  }

  async function register(data) {
    const res = await studentApi.register(data)
    student.value = res.data
    localStorage.setItem('student', JSON.stringify(res.data))
    return res.data
  }

  function logout() {
    student.value = null
    localStorage.removeItem('student')
  }

  return { student, isLoggedIn, currentStudent, login, register, logout }
})
