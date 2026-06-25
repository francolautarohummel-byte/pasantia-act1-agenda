export default function Button({ children, onClick, variant = 'primary', type = 'button', className = '' }) {
  return (
    <button
      type={type}
      onClick={onClick}
      className={`btn btn-${variant} d-inline-flex align-items-center gap-1 ${className}`}
    >
      {children}
    </button>
  );
}