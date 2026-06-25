import axios from 'axios';

const API = axios.create({
  baseURL: 'http://localhost:8080/api', // Tu backend en Spring Boot
  headers: {
    'Content-Type': 'application/json',
  },
});

// Interceptor opcional: Por si en el futuro decides añadir tokens JWT de seguridad
API.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default API;