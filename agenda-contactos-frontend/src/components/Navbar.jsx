import { Link, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

export default function Navbar() {
  const { logout } = useAuth();
  const location = useLocation();
  const usuarioId = localStorage.getItem('usuarioId');

  // No renderizar Navbar en Login o Register
  if (!usuarioId || location.pathname === '/login' || location.pathname === '/register') {
    return null;
  }

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark px-3">
      <Link className="navbar-brand" to="/dashboard">📇 Agenda App</Link>
      <div className="navbar-nav ms-auto flex-row gap-3">
        <Link className="nav-link text-white" to="/dashboard">Contactos</Link>
        <Link className="nav-link text-white" to="/perfil">Mi Perfil</Link>
        <button className="btn btn-outline-danger btn-sm" onClick={logout}>Cerrar Sesión</button>
      </div>
    </nav>
  );
}