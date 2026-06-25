import API from './api';

export const listarContactosPorUsuario = async (usuarioId) => {
  const response = await API.get(`/contacto/usuario/${usuarioId}`);
  return response.data;
};

export const crearContacto = async (contactoData, usuarioId) => {
  // Ajustado a como definiste tu @PostMapping con @RequestParam en el backend anterior
  const response = await API.post(`/contacto?usuarioId=${usuarioId}`, contactoData);
  return response.data;
};

// Métodos pendientes que implementarás en tu backend (Update / Delete)
export const eliminarContacto = async (contactoId) => {
  const response = await API.delete(`/contacto/${contactoId}`);
  return response.data;
};