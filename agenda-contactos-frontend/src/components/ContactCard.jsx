import Button from './atomic/Button';

export default function ContactCard({ contacto, onEdit, onDelete }) {
  return (
    <div className="card shadow-sm h-100">
      <div className="card-body d-flex flex-column justify-content-between">
        <div>
          <h5 className="card-title mb-1 text-capitalize">
            {contacto.nombre} {contacto.apellido}
          </h5>
          <p className="card-text text-muted small mb-2">
            📞 {contacto.telefono || 'Sin teléfono'}
          </p>
          <p className="card-text text-muted small mb-3">
            ✉️ {contacto.email || 'Sin email'}
          </p>
        </div>

        {/* Botones de Acción de la Tarjeta */}
        <div className="d-flex justify-content-end gap-2 pt-2 border-top">
          <Button variant="outline-warning" onClick={() => onEdit(contacto)}>
            ✏️ <span className="d-none d-sm-inline">Editar</span>
          </Button>
          <Button variant="outline-danger" onClick={() => onDelete(contacto.id)}>
            🗑️ <span className="d-none d-sm-inline">Eliminar</span>
          </Button>
        </div>
      </div>
    </div>
  );
}