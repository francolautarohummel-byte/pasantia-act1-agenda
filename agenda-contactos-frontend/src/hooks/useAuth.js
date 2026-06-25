import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { registrarUsuario } from '../services/usuario.service';
import API from '../services/api';

export function useAuth() {
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const login = async (email, password) => {
    setLoading(true);
    setError('');
    try {
      const response = await API.post('/usuario/login', { email, password });
      localStorage.setItem('usuarioId', response.data.id);
      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data || 'Credenciales inválidas.');
    } finally {
      setLoading(false);
    }
  };

  const register = async (userData) => {
    setLoading(true);
    setError('');
    try {
      await registrarUsuario(userData);
      navigate('/login');
    } catch (err) {
      setError(err.response?.data || 'Error en el registro.');
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    localStorage.removeItem('usuarioId');
    navigate('/login');
  };

  return { login, register, logout, error, loading };
}