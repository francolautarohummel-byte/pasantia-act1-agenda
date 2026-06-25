import { useState } from 'react';
import { useContacto } from '../hooks/useContacto';
import ContactGrid from '../components/ContactGrid';

export default function Dashboard() {
  const usuarioId = localStorage.getItem('usuarioId') || 1; // ID temporal si no hay login
  const { 
    contactos, 
    busqueda, 
    setBusqueda, 
    loading, 
    agregarContacto, 
    editarContactoExistente, 
    borrarContacto 
  } = useContacto(usuarioId);

  // Estado para controlar el formulario (Creación / Edición)
  const [showForm, setShowForm] = useState(false);
  const [contactoSeleccionado, setContactoSeleccionado] = useState(null);
  const [formData, setFormData] = useState({ nombre: '', apellido: '', telefono: '', email: '', direccion: '', observaciones: '' });

  // Manejar cambios de los inputs
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  // Activar modo "Crear"
  const handleAbrirCrear = () => {
    setContactoSeleccionado(null);
    setFormData({ nombre: '', apellido: '', telefono: '', email: '', direccion: '', observaciones: '' });
    setShowForm(true);
  };

  // Activar modo "Editar" cargando los datos viejos
  const handleAbrirEditar = (contacto) => {
    setContactoSeleccionado(contacto);
    setFormData(contacto); // Rellena el formulario con los datos del contacto
    setShowForm(true);
  };

  // Guardar datos (POST o PUT)
  const handleGuardar = async (e) => {
    e.preventDefault();
    let exito = false;

    if (contactoSeleccionado) {
      // Si hay seleccionado, estamos EDITANDO
      exito = await editarContactoExistente(contactoSeleccionado.id, formData);
    } else {
      // Si no, estamos CREANDO
      exito = await agregarContacto(formData);
    }

    if (exito) {
      setShowForm(false);
      setContactoSeleccionado(null);
    }
  };

  const handleEliminar = async (id) => {
    if (window.confirm("¿Seguro que quieres borrar este contacto?")) {
      await borrarContacto(id);
    }
  };

  if (loading) return <div className="text-center mt-5"><h4>Cargando agenda...</h4></div>;

  return (
    <div className="container py-4">
      
      {/* 🔍 Buscador por nombre */}
      <div className="mb-4">
        <input 
          type="text" 
          className="form-control form-control-lg shadow-sm" 
          placeholder="🔍 Buscar contacto por nombre o apellido..." 
          value={busqueda}
          onChange={(e) => setBusqueda(e.target.value)}
        />
      </div>

      {/* 📝 Formulario colapsable (Crear / Editar) */}
      {showForm && (
        <div className="card p-4 mb-4 shadow-sm bg-light">
          <h5>{contactoSeleccionado ? '✏️ Editar Contacto' : '➕ Nuevo Contacto'}</h5>
          <form onSubmit={handleGuardar} className="row g-3">
            <div className="col-md-6">
              <label className="form-label">Nombre</label>
              <input type="text" name="nombre" className="form-control" required value={formData.nombre} onChange={handleChange} />
            </div>
            <div className="col-md-6">
              <label className="form-label">Apellido</label>
              <input type="text" name="apellido" className="form-control" required value={formData.apellido} onChange={handleChange} />
            </div>
            <div className="col-md-6">
              <label className="form-label">Teléfono</label>
              <input type="text" name="telefono" className="form-control" value={formData.telefono} onChange={handleChange} />
            </div>
            <div className="col-md-6">
              <label className="form-label">Email</label>
              <input type="email" name="email" className="form-control" value={formData.email} onChange={handleChange} />
            </div>
            <div className="col-12">
              <button type="submit" className="btn btn-primary me-2">Guardar</button>
              <button type="button" className="btn btn-secondary" onClick={() => setShowForm(false)}>Cancelar</button>
            </div>
          </form>
        </div>
      )}

      {/* 🗂️ Reutilizamos tu Grid de Contactos con los botones funcionales */}
      <ContactGrid 
        contactos={contactos}
        onCreateNew={handleAbrirCrear}
        onEditContact={handleAbrirEditar}
        onDeleteContact={handleEliminar}
      />
    </div>
  );
}