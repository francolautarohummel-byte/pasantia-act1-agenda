package com.pasantia.service.impl;

import com.pasantia.entity.Contacto;
import com.pasantia.entity.Usuario;
import com.pasantia.repository.ContactoRepository;
import com.pasantia.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ContactoServiceImpl — Unit Tests")
class ContactoServiceImplTest {

    @Mock
    private ContactoRepository contactoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ContactoServiceImpl contactoService;

    // ── fixtures ─────────────────────────────────────────────────────────────

    private Usuario usuarioBase() {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setNombre("Juan Pérez");
        u.setEmail("juan@example.com");
        u.setPassword("pass123");
        return u;
    }

    private Contacto contactoBase(Usuario usuario) {
        Contacto c = new Contacto();
        c.setId(10L);
        c.setNombre("Carlos");
        c.setApellido("Rodriguez");
        c.setTelefono("1123456789");
        c.setEmail("carlos@gmail.com");
        c.setDireccion("Av. Corrientes 1234");
        c.setObservaciones("Cliente frecuente");
        c.setUsuario(usuario);
        return c;
    }

    // ── guardarContacto ───────────────────────────────────────────────────────

    @Nested
    @DisplayName("guardarContacto()")
    class GuardarContacto {

        @Test
        @DisplayName("asocia el usuario y persiste el contacto correctamente")
        void guarda_exitosamente() {
            Usuario usuario = usuarioBase();
            Contacto nuevo = new Contacto();
            nuevo.setNombre("Carlos");
            nuevo.setApellido("Rodriguez");
            nuevo.setTelefono("1123456789");

            Contacto guardado = contactoBase(usuario);

            when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
            when(contactoRepository.save(nuevo)).thenReturn(guardado);

            Contacto resultado = contactoService.guardarContacto(nuevo, 1L);

            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(10L);
            assertThat(resultado.getUsuario()).isEqualTo(usuario);
            verify(contactoRepository).save(nuevo);
        }

        @Test
        @DisplayName("lanza RuntimeException cuando el usuarioId no existe")
        void lanza_excepcion_usuario_no_encontrado() {
            Contacto nuevo = new Contacto();
            when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> contactoService.guardarContacto(nuevo, 99L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("99");

            verify(contactoRepository, never()).save(any());
        }
    }

    // ── listarContactosPorUsuario ─────────────────────────────────────────────

    @Nested
    @DisplayName("listarContactosPorUsuario()")
    class ListarContactos {

        @Test
        @DisplayName("retorna la lista de contactos del usuario")
        void lista_contactos() {
            Usuario usuario = usuarioBase();
            List<Contacto> lista = List.of(contactoBase(usuario));
            when(contactoRepository.findByUsuarioId(1L)).thenReturn(lista);

            List<Contacto> resultado = contactoService.listarContactosPorUsuario(1L);

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getNombre()).isEqualTo("Carlos");
        }

        @Test
        @DisplayName("retorna lista vacía cuando el usuario no tiene contactos")
        void lista_vacia() {
            when(contactoRepository.findByUsuarioId(1L)).thenReturn(List.of());

            List<Contacto> resultado = contactoService.listarContactosPorUsuario(1L);

            assertThat(resultado).isEmpty();
        }
    }

    // ── actualizarContacto ────────────────────────────────────────────────────

    @Nested
    @DisplayName("actualizarContacto()")
    class ActualizarContacto {

        @Test
        @DisplayName("actualiza todos los campos del contacto existente")
        void actualiza_exitosamente() {
            Usuario usuario = usuarioBase();
            Contacto existente = contactoBase(usuario);

            Contacto detalles = new Contacto();
            detalles.setNombre("Lucia");
            detalles.setApellido("Fernandez");
            detalles.setTelefono("1165432198");
            detalles.setEmail("lucia@gmail.com");
            detalles.setDireccion("San Martin 456");
            detalles.setObservaciones("Prefiere WhatsApp");

            when(contactoRepository.findById(10L)).thenReturn(Optional.of(existente));
            when(contactoRepository.save(existente)).thenReturn(existente);

            Contacto resultado = contactoService.actualizarContacto(10L, detalles);

            assertThat(resultado.getNombre()).isEqualTo("Lucia");
            assertThat(resultado.getApellido()).isEqualTo("Fernandez");
            assertThat(resultado.getTelefono()).isEqualTo("1165432198");
            assertThat(resultado.getEmail()).isEqualTo("lucia@gmail.com");
            assertThat(resultado.getDireccion()).isEqualTo("San Martin 456");
            assertThat(resultado.getObservaciones()).isEqualTo("Prefiere WhatsApp");
            verify(contactoRepository).save(existente);
        }

        @Test
        @DisplayName("lanza RuntimeException cuando el contactoId no existe")
        void lanza_excepcion_contacto_no_encontrado() {
            when(contactoRepository.findById(99L)).thenReturn(Optional.empty());

            Contacto detalles = new Contacto();
            detalles.setNombre("X");

            assertThatThrownBy(() -> contactoService.actualizarContacto(99L, detalles))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("99");

            verify(contactoRepository, never()).save(any());
        }
    }

    // ── eliminarContacto ──────────────────────────────────────────────────────

    @Nested
    @DisplayName("eliminarContacto()")
    class EliminarContacto {

        @Test
        @DisplayName("elimina el contacto cuando el ID existe")
        void elimina_exitosamente() {
            Usuario usuario = usuarioBase();
            Contacto existente = contactoBase(usuario);

            when(contactoRepository.findById(10L)).thenReturn(Optional.of(existente));

            contactoService.eliminarContacto(10L);

            verify(contactoRepository).delete(existente);
        }

        @Test
        @DisplayName("lanza RuntimeException cuando el contactoId no existe")
        void lanza_excepcion_contacto_no_encontrado() {
            when(contactoRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> contactoService.eliminarContacto(99L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("99");

            verify(contactoRepository, never()).delete(any());
        }
    }

    // ── buscarContactos ───────────────────────────────────────────────────────

    @Nested
    @DisplayName("buscarContactos()")
    class BuscarContactos {

        @Test
        @DisplayName("delega al repositorio y retorna los resultados")
        void busca_por_nombre_o_apellido() {
            Usuario usuario = usuarioBase();
            List<Contacto> lista = List.of(contactoBase(usuario));
            when(contactoRepository.buscarPorNombreOApellido(1L, "Car")).thenReturn(lista);

            List<Contacto> resultado = contactoService.buscarContactos(1L, "Car");

            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getNombre()).isEqualTo("Carlos");
            verify(contactoRepository).buscarPorNombreOApellido(1L, "Car");
        }

        @Test
        @DisplayName("retorna lista vacía cuando no hay coincidencias")
        void retorna_vacio_sin_coincidencias() {
            when(contactoRepository.buscarPorNombreOApellido(1L, "XYZ")).thenReturn(List.of());

            List<Contacto> resultado = contactoService.buscarContactos(1L, "XYZ");

            assertThat(resultado).isEmpty();
        }
    }
}
