package com.pasantia.service.impl;

import com.pasantia.entity.Usuario;
import com.pasantia.repository.UsuarioRepository;
import com.pasantia.service.UsuarioService;
import lombok.RequiredArgsConstructor; // 👈 NUEVO IMPORT
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor // 👈 REEMPLAZA EL AUTOWIRED GENERANDO EL CONSTRUCTOR AUTOMÁTICAMENTE
public class UsuarioServiceImpl implements UsuarioService {

    // Cambiado a 'private final' sin @Autowired
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya se encuentra registrado");
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> obtenerPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Agrega estos métodos al final de la clase UsuarioServiceImpl

    @Override
    @Transactional
    public Usuario modificarPerfil(Long id, Usuario usuarioDetalles) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Permitir modificar nombre y correo electrónico
        usuarioExistente.setNombre(usuarioDetalles.getNombre());

        // Si cambia de email, validar que no esté duplicado con otro usuario
        if (!usuarioExistente.getEmail().equals(usuarioDetalles.getEmail())) {
            if (usuarioRepository.findByEmail(usuarioDetalles.getEmail()).isPresent()) {
                throw new RuntimeException("El correo electrónico ya está en uso por otro usuario");
            }
            usuarioExistente.setEmail(usuarioDetalles.getEmail());
        }

        // Opcional: si envía una nueva contraseña, actualizarla
        if (usuarioDetalles.getPassword() != null && !usuarioDetalles.getPassword().isBlank()) {
            usuarioExistente.setPassword(usuarioDetalles.getPassword());
        }

        return usuarioRepository.save(usuarioExistente);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario iniciarSesion(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Credenciales incorrectas (Email no encontrado)"));

        // Validación simple de contraseña de texto plano
        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Credenciales incorrectas (Contraseña inválida)");
        }

        return usuario; // Retorna el usuario logueado exitosamente
    }

}
