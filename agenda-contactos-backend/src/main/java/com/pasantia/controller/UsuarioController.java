package com.pasantia.controller;

import com.pasantia.dto.UsuarioRegistroDTO;
import com.pasantia.dto.UsuarioResponseDTO;
import com.pasantia.dto.LoginDTO;
import com.pasantia.entity.Usuario;
import com.pasantia.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
@RequiredArgsConstructor

public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioRegistroDTO registroDTO) {
        try {
            Usuario usuario = new Usuario();
            usuario.setNombre(registroDTO.getNombre());
            usuario.setEmail(registroDTO.getEmail());
            usuario.setPassword(registroDTO.getPassword());

            Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);

            return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponseDTO(nuevoUsuario));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioPorId(@PathVariable Long id) {
        return usuarioService.obtenerPorId(id)
                .map(usuario -> ResponseEntity.ok(convertToResponseDTO(usuario)))
                .orElse(ResponseEntity.notFound().build());
    }

    // Método helper para convertir Entidad -> DTO
    private UsuarioResponseDTO convertToResponseDTO(Usuario usuario) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setFechaCreacion(usuario.getFechaCreacion());
        return dto;
    }

    // Agrega este import arriba

    // Dentro de la clase UsuarioController, debajo de registrarUsuario...

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        try {
            // Usa directamente tu método del Service
            Usuario usuario = usuarioService.iniciarSesion(loginDTO.getEmail(), loginDTO.getPassword());

            if (usuario != null) {
                // Mapeamos a un ResponseDTO para no enviar la contraseña de vuelta por el JSON
                UsuarioResponseDTO responseDTO = new UsuarioResponseDTO();
                responseDTO.setId(usuario.getId());
                responseDTO.setNombre(usuario.getNombre());
                responseDTO.setEmail(usuario.getEmail());
                responseDTO.setFechaCreacion(usuario.getFechaCreacion());

                return ResponseEntity.ok(responseDTO);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
            }
        } catch (RuntimeException e) {
            // Si tu implementación lanza excepciones (ej: "Usuario no encontrado"), las
            // atrapamos acá
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> modificarPerfil(@PathVariable Long id, @RequestBody UsuarioRegistroDTO registroDTO) {
        try {
            // Creamos la entidad temporal con los nuevos detalles que vienen del frontend
            Usuario detalles = new Usuario();
            detalles.setNombre(registroDTO.getNombre());
            detalles.setEmail(registroDTO.getEmail());
            detalles.setPassword(registroDTO.getPassword()); // Por si decide actualizar la clave

            // Llamamos a tu método del Service
            Usuario usuarioActualizado = usuarioService.modificarPerfil(id, detalles);

            return ResponseEntity.ok(convertToResponseDTO(usuarioActualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
