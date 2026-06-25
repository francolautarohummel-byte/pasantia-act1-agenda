import API from './api';

export const registrarUsuario = async (usuarioData) => {
  const response = await API.post('/usuario', usuarioData);
  return response.data;
};

export const obtenerUsuarioPorId = async (id) => {
  const response = await API.get(`/usuario/${id}`);
  return response.data;
};

export const actualizarUsuario = async (id, usuarioData) => {
  const response = await API.put(`/usuario/${id}`, usuarioData);
  return response.data;
};