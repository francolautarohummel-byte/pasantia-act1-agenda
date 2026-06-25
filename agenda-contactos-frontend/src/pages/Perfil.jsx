import { useState, useEffect } from 'react';
import { obtenerUsuarioPorId, actualizarUsuario } from '../services/usuario.service';

export default function Perfil() {
  const [formData, setFormData] = useState({ nombre: '', email: '', password: '' });
  const [loading, setLoading] = useState(true);
  const [mensaje, setMensaje] = useState({ texto: '', tipo: '' });
  
  // Obtenemos el ID del usuario logueado
  const usuarioId = localStorage.getItem('usuarioId');

  useEffect(() => {
    if (usuarioId) {
      obtenerUsuarioPorId(usuarioId)
        .then((data) => {
          // Cargamos los datos del backend, dejando el password vacío por seguridad
          setFormData({ nombre: data.nombre, email: data.email, password: '' });
          setLoading(false);
        })
        .catch((err) => {
          setMensaje({ texto: 'Error al cargar los datos del usuario.', tipo: 'danger' });
          setLoading(false);
        });
    }
  }, [usuarioId]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setMensaje({ texto: '', tipo: '' });
    try {
      await actualizarUsuario(usuarioId, formData);
      setMensaje({ texto: '¡Perfil actualizado correctamente!', tipo: 'success' });
    } catch (err) {
      setMensaje({ texto: err.response?.data || 'Error al actualizar el perfil.', tipo: 'danger' });
    }
  };

  if (loading) return <div className="text-center mt-5"><h5>Cargando perfil...</h5></div>;

  return (
    <div className="row justify-content-center">
      <div className="col-md-6">
        <div className="card p-4 shadow-sm">
          <h3 className="mb-4">Mi Perfil</h3>
          {mensaje.texto && <div className={`alert alert-${mensaje.tipo}`}>{mensaje.texto}</div>}
          
          <form onSubmit={handleSubmit}>
            <div className="mb-3">
              <label className="form-label">Nombre</label>
              <input type="text" name="nombre" className="form-control" required value={formData.nombre} onChange={handleChange} />
            </div>
            <div className="mb-3">
              <label className="form-label">Correo Electrónico</label>
              <input type="email" name="email" className="form-control" required value={formData.email} onChange={handleChange} />
            </div>
            <div className="mb-3">
              <label className="form-label">Nueva Contraseña (Dejar en blanco para no cambiar)</label>
              <input type="password" name="password" className="form-control" placeholder="Escribe un nuevo password si deseas cambiarlo" value={formData.password} onChange={handleChange} />
            </div>
            <button type="submit" className="btn btn-primary">Guardar Cambios</button>
          </form>
        </div>
      </div>
    </div>
  );
}