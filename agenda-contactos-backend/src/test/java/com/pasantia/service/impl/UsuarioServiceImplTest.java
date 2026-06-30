package com.pasantia.service.impl;

import com.pasantia.entity.Usuario;
import com.pasantia.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioServiceImpl — Unit Tests")
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    // ── fixtures ─────────────────────────────────────────────────────────────

    private Usuario usuarioBase() {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setNombre("Juan Pérez");
        u.setEmail("juan@example.com");
        u.setPassword("pass123");
        u.setFechaCreacion(LocalDateTime.now());
        return u;
    }

    // ── registrarUsuario ──────────────────────────────────────────────────────

    @Nested
    @DisplayName("registrarUsuario()")
    class RegistrarUsuario {

        @Test
        @DisplayName("registra y retorna el usuario cuando el email no existe")
        void registra_exitosamente() {
            Usuario nuevo = usuarioBase();
            nuevo.setId(null);

            when(usuarioRepository.findByEmail(nuevo.getEmail())).thenReturn(Optional.empty());
            when(usuarioRepository.save(nuevo)).thenReturn(usuarioBase());

            Usuario resultado = usuarioService.registrarUsuario(nuevo);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getNombre()).isEqualTo("Juan Pérez");
            verify(usuarioRepository).save(nuevo);
        }

        @Test
        @DisplayName("lanza RuntimeException si el email ya está registrado")
        void lanza_excepcion_email_duplicado() {
            Usuario nuevo = usuarioBase();
            when(usuarioRepository.findByEmail(nuevo.getEmail())).thenReturn(Optional.of(nuevo));

            assertThatThrownBy(() -> usuarioService.registrarUsuario(nuevo))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("ya se encuentra registrado");

            verify(usuarioRepository, never()).save(any());
        }
    }

    // ── obtenerPorId ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("obtenerPorId()")
    class ObtenerPorId {

        @Test
        @DisplayName("retorna Optional con el usuario cuando existe")
        void retorna_usuario_existente() {
            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuarioBase()));

            Optional<Usuario> resultado = usuarioService.obtenerPorId(1L);

            assertThat(resultado).isPresent();
            assertThat(resultado.get().getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("retorna Optional vacío cuando el ID no existe")
        void retorna_empty_si_no_existe() {
            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            Optional<Usuario> resultado = usuarioService.obtenerPorId(99L);

            assertThat(resultado).isEmpty();
        }
    }

    // ── obtenerPorEmail ───────────────────────────────────────────────────────

    @Nested
    @DisplayName("obtenerPorEmail()")
    class ObtenerPorEmail {

        @Test
        @DisplayName("retorna Optional con el usuario cuando el email existe")
        void retorna_usuario_por_email() {
            when(usuarioRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(usuarioBase()));

            Optional<Usuario> resultado = usuarioService.obtenerPorEmail("juan@example.com");

            assertThat(resultado).isPresent();
            assertThat(resultado.get().getEmail()).isEqualTo("juan@example.com");
        }

        @Test
        @DisplayName("retorna Optional vacío cuando el email no existe")
        void retorna_empty_si_email_no_existe() {
            when(usuarioRepository.findByEmail("noexiste@x.com")).thenReturn(Optional.empty());

            Optional<Usuario> resultado = usuarioService.obtenerPorEmail("noexiste@x.com");

            assertThat(resultado).isEmpty();
        }
    }

    // ── modificarPerfil ───────────────────────────────────────────────────────

    @Nested
    @DisplayName("modificarPerfil()")
    class ModificarPerfil {

        @Test
        @DisplayName("modifica nombre sin cambiar el email")
        void modifica_nombre_mismo_email() {
            Usuario existente = usuarioBase();
            Usuario detalles = new Usuario();
            detalles.setNombre("Nuevo Nombre");
            detalles.setEmail(existente.getEmail()); // mismo email
            detalles.setPassword("nuevaPass");

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(usuarioRepository.save(existente)).thenReturn(existente);

            Usuario resultado = usuarioService.modificarPerfil(1L, detalles);

            assertThat(resultado.getNombre()).isEqualTo("Nuevo Nombre");
            assertThat(resultado.getPassword()).isEqualTo("nuevaPass");
            // El email no cambia, por lo tanto findByEmail nunca se llama para validar
            // duplicados
            verify(usuarioRepository, never()).findByEmail(existente.getEmail());
        }

        @Test
        @DisplayName("cambia el email cuando no está en uso por otro usuario")
        void cambia_email_libre() {
            Usuario existente = usuarioBase();
            Usuario detalles = new Usuario();
            detalles.setNombre("Juan Pérez");
            detalles.setEmail("nuevo@example.com");
            detalles.setPassword(null); // sin cambio de contraseña

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(usuarioRepository.findByEmail("nuevo@example.com")).thenReturn(Optional.empty());
            when(usuarioRepository.save(existente)).thenReturn(existente);

            Usuario resultado = usuarioService.modificarPerfil(1L, detalles);

            assertThat(resultado.getEmail()).isEqualTo("nuevo@example.com");
        }

        @Test
        @DisplayName("no actualiza la contraseña cuando el campo viene en blanco")
        void no_actualiza_password_si_blank() {
            Usuario existente = usuarioBase();
            existente.setPassword("original");
            Usuario detalles = new Usuario();
            detalles.setNombre("Juan Pérez");
            detalles.setEmail(existente.getEmail());
            detalles.setPassword("   "); // blank

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(usuarioRepository.save(existente)).thenReturn(existente);

            Usuario resultado = usuarioService.modificarPerfil(1L, detalles);

            assertThat(resultado.getPassword()).isEqualTo("original");
        }

        @Test
        @DisplayName("lanza RuntimeException si el nuevo email ya pertenece a otro usuario")
        void lanza_excepcion_email_duplicado() {
            Usuario existente = usuarioBase();
            Usuario otro = new Usuario();
            otro.setId(2L);
            otro.setEmail("ocupado@example.com");

            Usuario detalles = new Usuario();
            detalles.setNombre("Juan");
            detalles.setEmail("ocupado@example.com");

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(existente));
            when(usuarioRepository.findByEmail("ocupado@example.com")).thenReturn(Optional.of(otro));

            assertThatThrownBy(() -> usuarioService.modificarPerfil(1L, detalles))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("ya está en uso");

            verify(usuarioRepository, never()).save(any());
        }

        @Test
        @DisplayName("lanza RuntimeException si el ID del usuario no existe")
        void lanza_excepcion_usuario_no_encontrado() {
            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            Usuario detalles = new Usuario();
            detalles.setNombre("X");
            detalles.setEmail("x@x.com");

            assertThatThrownBy(() -> usuarioService.modificarPerfil(99L, detalles))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("99");
        }
    }

    // ── iniciarSesion ─────────────────────────────────────────────────────────

    @Nested
    @DisplayName("iniciarSesion()")
    class IniciarSesion {

        @Test
        @DisplayName("retorna el usuario cuando las credenciales son correctas")
        void login_exitoso() {
            Usuario u = usuarioBase();
            when(usuarioRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(u));

            Usuario resultado = usuarioService.iniciarSesion("juan@example.com", "pass123");

            assertThat(resultado).isNotNull();
            assertThat(resultado.getEmail()).isEqualTo("juan@example.com");
        }

        @Test
        @DisplayName("lanza RuntimeException cuando el email no existe")
        void lanza_excepcion_email_no_encontrado() {
            when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

            assertThatThrownBy(() -> usuarioService.iniciarSesion("noexiste@x.com", "pass"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Email no encontrado");
        }

        @Test
        @DisplayName("lanza RuntimeException cuando la contraseña es incorrecta")
        void lanza_excepcion_password_incorrecta() {
            Usuario u = usuarioBase();
            when(usuarioRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(u));

            assertThatThrownBy(() -> usuarioService.iniciarSesion("juan@example.com", "malaClave"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Contraseña inválida");
        }
    }
}
