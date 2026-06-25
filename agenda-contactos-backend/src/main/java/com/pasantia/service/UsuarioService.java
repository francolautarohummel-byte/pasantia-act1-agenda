package com.pasantia.service;

import com.pasantia.entity.Usuario;
import java.util.Optional;

public interface UsuarioService {
    Usuario registrarUsuario(Usuario usuario);
    Optional<Usuario> obtenerPorId(Long id);
    Optional<Usuario> obtenerPorEmail(String email);
    Usuario modificarPerfil(Long id, Usuario usuarioDetalles);
    Usuario iniciarSesion(String email, String password);

}
