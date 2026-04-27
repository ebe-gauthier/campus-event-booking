import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  headers: { 'Content-Type': 'application/json' }
})

export const eventApi = {
  getAll: () => api.get('/events'),
  getById: (id) => api.get(`/events/${id}`),
  search: (q) => api.get(`/events/search?q=${q}`),
  getByCategory: (cat) => api.get(`/events/category/${cat}`),
  create: (data) => api.post('/events', data),
  update: (id, data) => api.put(`/events/${id}`, data),
  delete: (id) => api.delete(`/events/${id}`)
}

export const studentApi = {
  register: (data) => api.post('/students/register', data),
  login: (data) => api.post('/students/login', data),
  getById: (id) => api.get(`/students/${id}`),
  getAll: () => api.get('/students')
}

export const bookingApi = {
  book: (studentId, eventId) => api.post('/bookings', { studentId, eventId }),
  cancel: (studentId, eventId) => api.delete('/bookings/cancel', { data: { studentId, eventId } }),
  getByStudent: (studentId) => api.get(`/bookings/student/${studentId}`),
  getByEvent: (eventId) => api.get(`/bookings/event/${eventId}`)
}

export default api
