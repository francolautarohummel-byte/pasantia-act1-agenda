import Button from './atomic/Button';
import ContactCard from './ContactCard';


export default function ContactGrid({ contactos, onCreateNew, onEditContact, onDeleteContact }) {
  return (
    <div className="container-fluid px-0">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h4 className="mb-0 text-secondary">Mis Contactos ({contactos.length})</h4>
        
        {/* Botón para Crear */}
        <Button variant="success" onClick={onCreateNew}>
          ➕ Crear Contacto
        </Button>
      </div>

      {contactos.length === 0 ? (
        <div className="alert alert-info text-center py-4">
          No se encontraron contactos en tu agenda.
        </div>
      ) : (
        <div className="row g-3">
          {contactos.map((contacto) => (
            <div key={contacto.id} className="col-12 col-md-6 col-lg-4">
              <ContactCard 
                contacto={contacto} 
                onEdit={onEditContact} 
                onDelete={onDeleteContact} 
              />
            </div>
          ))}
        </div>
      )}
    </div>
  );
}