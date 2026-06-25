import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import API from '../services/api';

export default function Login() {
  const [formData, setFormData] = useState({ email: '', password: '' });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      // Endpoint simulado o real de login en tu backend.
      // Si no tienes endpoint de login aún, puedes buscarlo por ID o simularlo temporalmente:
      const response = await API.post('/usuario/login', formData); 
      
      // Guardamos el ID del usuario retornado para usarlo en toda la app
      localStorage.setItem('usuarioId', response.data.id);
      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data || 'Credenciales inválidas o error de conexión.');
    }
  };

  return (
    <div className="row justify-content-center">
      <div className="col-md-5">
        <div className="card p-4 mt-5 shadow-sm">
          <h3 className="text-center mb-4">Iniciar Sesión</h3>
          {error && <div className="alert alert-danger">{error}</div>}
          
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label className="form-label">Correo Electrónico</label>
              <input type="email" name="email" className="form-control" required value={formData.email} onChange={handleChange} />
            </div>
            <div className="mb-3">
              <label className="form-label">Contraseña</label>
              <input type="password" name="password" className="form-control" required value={formData.password} onChange={handleChange} />
            </div>
            <button type="submit" className="btn btn-success w-100">Ingresar</button>
          </form>
          <p className="text-center mt-3 mb-0">
            ¿No tienes cuenta? <Link to="/register">Regístrate aquí</Link>
          </p>
        </div>
      </div>
    </div>
  );
}