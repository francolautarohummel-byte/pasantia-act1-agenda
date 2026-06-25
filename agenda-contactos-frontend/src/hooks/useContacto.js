import { useState, useEffect } from 'react';
import { 
  listarContactosPorUsuario, 
  crearContacto,
  // Asumiendo que tienes estos métodos exportados en tu contacto.service.js:
  // actualizarContacto, 
  // eliminarContacto 
} from '../services/contacto.service';
import API from '../services/api'; // Por si necesitas llamadas directas rápidas

export function useContacto(usuarioId) {
  const [contactos, setContactos] = useState([]);
  const [busqueda, setBusqueda] = useState('');
  const [loading, setLoading] = useState(true);

  // 1. LEER (Listar)
  const cargarContactos = async () => {
    if (!usuarioId) return;
    setLoading(true);
    try {
      const data = await listarContactosPorUsuario(usuarioId);
      setContactos(data);
    } catch (err) {
      console.error("Error al traer contactos:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    cargarContactos();
  }, [usuarioId]);

  // Lógica de filtrado por nombre en tiempo real
  const contactosFiltrados = contactos.filter(c =>
    `${c.nombre} ${c.apellido}`.toLowerCase().includes(busqueda.toLowerCase())
  );

  // 2. CREAR
  const agregarContacto = async (contactoData) => {
    try {
      const nuevo = await crearContacto(contactoData, usuarioId);
      setContactos(prev => [...prev, nuevo]); // Lo sumamos a la lista
      return true;
    } catch (err) {
      console.error("Error al crear:", err);
      return false;
    }
  };

  // 3. ACTUALIZAR (Editar)
  const editarContactoExistente = async (id, contactoData) => {
    try {
      // Reemplaza con tu método de servicio si ya lo definiste
      const response = await API.put(`/contacto/${id}`, contactoData);
      setContactos(prev => prev.map(c => c.id === id ? response.data : c));
      return true;
    } catch (err) {
      console.error("Error al editar:", err);
      return false;
    }
  };

  // 4. ELIMINAR (Borrar)
  const borrarContacto = async (id) => {
    try {
      await API.delete(`/contacto/${id}`);
      setContactos(prev => prev.filter(c => c.id !== id)); // Lo sacamos de la lista
      return true;
    } catch (err) {
      console.error("Error al eliminar:", err);
      return false;
    }
  };

  return {
    contactos: contactosFiltrados,
    busqueda,
    setBusqueda,
    loading,
    agregarContacto,
    editarContactoExistente,
    borrarContacto
  };
}