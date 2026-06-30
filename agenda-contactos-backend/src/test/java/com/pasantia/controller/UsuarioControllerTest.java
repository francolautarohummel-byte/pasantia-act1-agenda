package com.pasantia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasantia.dto.LoginDTO;
import com.pasantia.dto.UsuarioRegistroDTO;
import com.pasantia.entity.Usuario;
import com.pasantia.service.UsuarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UsuarioController — Unit Tests")
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(usuarioController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // soporte para LocalDateTime
    }

    // ── fixture ───────────────────────────────────────────────────────────────

    private Usuario usuarioBase() {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setNombre("Juan Pérez");
        u.setEmail("juan@example.com");
        u.setPassword("pass123");
        u.setFechaCreacion(LocalDateTime.of(2026, 1, 15, 10, 0));
        return u;
    }

    private UsuarioRegistroDTO registroDTOBase() {
        UsuarioRegistroDTO dto = new UsuarioRegistroDTO();
        dto.setNombre("Juan Pérez");
        dto.setEmail("juan@example.com");
        dto.setPassword("pass123");
        return dto;
    }

    // ── POST /api/usuario ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("POST /api/usuario  (registrarUsuario)")
    class RegistrarUsuario {

        @Test
        @DisplayName("retorna 201 CREATED con el UsuarioResponseDTO cuando el registro es exitoso")
        void registro_exitoso_201() throws Exception {
            when(usuarioService.registrarUsuario(any(Usuario.class))).thenReturn(usuarioBase());

            mockMvc.perform(post("/api/usuario")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registroDTOBase())))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nombre").value("Juan Pérez"))
                    .andExpect(jsonPath("$.email").value("juan@example.com"))
                    .andExpect(jsonPath("$.password").doesNotExist()); // nunca exponer password
        }

        @Test
        @DisplayName("retorna 400 BAD REQUEST cuando el servicio lanza RuntimeException")
        void registro_falla_400() throws Exception {
            when(usuarioService.registrarUsuario(any(Usuario.class)))
                    .thenThrow(new RuntimeException("El correo electrónico ya se encuentra registrado"));

            mockMvc.perform(post("/api/usuario")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registroDTOBase())))
                    .andExpect(status().isBadRequest());
        }
    }

    // ── GET /api/usuario/{id} ─────────────────────────────────────────────────

    @Nested
    @DisplayName("GET /api/usuario/{id}  (obtenerUsuarioPorId)")
    class ObtenerUsuarioPorId {

        @Test
        @DisplayName("retorna 200 OK con el usuario cuando el ID existe")
        void obtener_existente_200() throws Exception {
            when(usuarioService.obtenerPorId(1L)).thenReturn(Optional.of(usuarioBase()));

            mockMvc.perform(get("/api/usuario/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.nombre").value("Juan Pérez"));
        }

        @Test
        @DisplayName("retorna 404 NOT FOUND cuando el ID no existe")
        void obtener_no_existente_404() throws Exception {
            when(usuarioService.obtenerPorId(99L)).thenReturn(Optional.empty());

            mockMvc.perform(get("/api/usuario/99"))
                    .andExpect(status().isNotFound());
        }
    }

    // ── POST /api/usuario/login ───────────────────────────────────────────────

    @Nested
    @DisplayName("POST /api/usuario/login  (login)")
    class Login {

        private LoginDTO loginDTO() {
            LoginDTO dto = new LoginDTO();
            dto.setEmail("juan@example.com");
            dto.setPassword("pass123");
            return dto;
        }

        @Test
        @DisplayName("retorna 200 OK con el UsuarioResponseDTO en login exitoso")
        void login_exitoso_200() throws Exception {
            when(usuarioService.iniciarSesion("juan@example.com", "pass123")).thenReturn(usuarioBase());

            mockMvc.perform(post("/api/usuario/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginDTO())))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.email").value("juan@example.com"))
                    .andExpect(jsonPath("$.password").doesNotExist());
        }

        @Test
        @DisplayName("retorna 401 UNAUTHORIZED cuando el servicio lanza RuntimeException")
        void login_fallido_401() throws Exception {
            when(usuarioService.iniciarSesion(anyString(), anyString()))
                    .thenThrow(new RuntimeException("Credenciales incorrectas"));

            mockMvc.perform(post("/api/usuario/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginDTO())))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("retorna 401 cuando iniciarSesion retorna null")
        void login_null_401() throws Exception {
            when(usuarioService.iniciarSesion(anyString(), anyString())).thenReturn(null);

            mockMvc.perform(post("/api/usuario/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginDTO())))
                    .andExpect(status().isUnauthorized());
        }
    }

    // ── PUT /api/usuario/{id} ─────────────────────────────────────────────────

    @Nested
    @DisplayName("PUT /api/usuario/{id}  (modificarPerfil)")
    class ModificarPerfil {

        @Test
        @DisplayName("retorna 200 OK con el usuario actualizado")
        void modificar_exitoso_200() throws Exception {
            Usuario actualizado = usuarioBase();
            actualizado.setNombre("Juan Modificado");
            when(usuarioService.modificarPerfil(eq(1L), any(Usuario.class))).thenReturn(actualizado);

            mockMvc.perform(put("/api/usuario/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registroDTOBase())))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.nombre").value("Juan Modificado"));
        }

        @Test
        @DisplayName("retorna 400 BAD REQUEST cuando el servicio lanza RuntimeException")
        void modificar_falla_400() throws Exception {
            when(usuarioService.modificarPerfil(eq(1L), any(Usuario.class)))
                    .thenThrow(new RuntimeException("El correo electrónico ya está en uso"));

            mockMvc.perform(put("/api/usuario/1")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(registroDTOBase())))
                    .andExpect(status().isBadRequest());
        }
    }
}
