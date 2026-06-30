package com.pasantia.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pasantia.dto.ContactoRequestDTO;
import com.pasantia.entity.Contacto;
import com.pasantia.entity.Usuario;
import com.pasantia.service.ContactoService;
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

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ContactoController — Unit Tests")
class ContactoControllerTest {

    @Mock
    private ContactoService contactoService;

    @InjectMocks
    private ContactoController contactoController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(contactoController).build();
        objectMapper = new ObjectMapper();
    }

    // ── fixtures ─────────────────────────────────────────────────────────────

    private Usuario usuarioBase() {
        Usuario u = new Usuario();
        u.setId(1L);
        u.setNombre("Juan Pérez");
        u.setEmail("juan@example.com");
        return u;
    }

    private Contacto contactoBase() {
        Contacto c = new Contacto();
        c.setId(10L);
        c.setNombre("Carlos");
        c.setApellido("Rodriguez");
        c.setTelefono("1123456789");
        c.setEmail("carlos@gmail.com");
        c.setDireccion("Av. Corrientes 1234");
        c.setObservaciones("Cliente frecuente");
        c.setUsuario(usuarioBase());
        return c;
    }

    private ContactoRequestDTO requestDTOBase() {
        ContactoRequestDTO dto = new ContactoRequestDTO();
        dto.setNombre("Carlos");
        dto.setApellido("Rodriguez");
        dto.setTelefono("1123456789");
        dto.setEmail("carlos@gmail.com");
        dto.setDireccion("Av. Corrientes 1234");
        dto.setObservaciones("Cliente frecuente");
        return dto;
    }

    // ── POST /api/contacto ────────────────────────────────────────────────────

    @Nested
    @DisplayName("POST /api/contacto  (crearContacto)")
    class CrearContacto {

        @Test
        @DisplayName("retorna 201 CREATED con el ContactoResponseDTO cuando la creación es exitosa")
        void crear_exitoso_201() throws Exception {
            when(contactoService.guardarContacto(any(Contacto.class), eq(1L))).thenReturn(contactoBase());

            mockMvc.perform(post("/api/contacto")
                            .param("usuarioId", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTOBase())))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(10))
                    .andExpect(jsonPath("$.nombre").value("Carlos"))
                    .andExpect(jsonPath("$.apellido").value("Rodriguez"))
                    .andExpect(jsonPath("$.telefono").value("1123456789"))
                    .andExpect(jsonPath("$.email").value("carlos@gmail.com"))
                    .andExpect(jsonPath("$.direccion").value("Av. Corrientes 1234"))
                    .andExpect(jsonPath("$.observaciones").value("Cliente frecuente"))
                    .andExpect(jsonPath("$.usuarioId").value(1));
        }

        @Test
        @DisplayName("retorna 400 BAD REQUEST cuando el servicio lanza RuntimeException")
        void crear_falla_400() throws Exception {
            when(contactoService.guardarContacto(any(Contacto.class), eq(99L)))
                    .thenThrow(new RuntimeException("Usuario no encontrado con ID: 99"));

            mockMvc.perform(post("/api/contacto")
                            .param("usuarioId", "99")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(requestDTOBase())))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("mapea correctamente todos los campos del RequestDTO al Contacto")
        void mapeo_campos_request_dto() throws Exception {
            Contacto contactoSinUsuario = new Contacto();
            contactoSinUsuario.setId(11L);
            contactoSinUsuario.setNombre("Lucia");
            contactoSinUsuario.setApellido("Fernandez");
            contactoSinUsuario.setTelefono("1165432198");
            contactoSinUsuario.setEmail("lucia@gmail.com");
            contactoSinUsuario.setDireccion("San Martin 456");
            contactoSinUsuario.setObservaciones("Prefiere WhatsApp");
            // sin usuario → usuarioId null en el response

            when(contactoService.guardarContacto(any(Contacto.class), eq(1L))).thenReturn(contactoSinUsuario);

            ContactoRequestDTO dto = new ContactoRequestDTO();
            dto.setNombre("Lucia");
            dto.setApellido("Fernandez");
            dto.setTelefono("1165432198");
            dto.setEmail("lucia@gmail.com");
            dto.setDireccion("San Martin 456");
            dto.setObservaciones("Prefiere WhatsApp");

            mockMvc.perform(post("/api/contacto")
                            .param("usuarioId", "1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.nombre").value("Lucia"))
                    .andExpect(jsonPath("$.telefono").value("1165432198"));
        }
    }

    // ── GET /api/contacto/usuario/{usuarioId} ─────────────────────────────────

    @Nested
    @DisplayName("GET /api/contacto/usuario/{usuarioId}  (listarContactos)")
    class ListarContactos {

        @Test
        @DisplayName("retorna 200 OK con la lista de contactos del usuario")
        void listar_exitoso_200() throws Exception {
            when(contactoService.listarContactosPorUsuario(1L)).thenReturn(List.of(contactoBase()));

            mockMvc.perform(get("/api/contacto/usuario/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(1))
                    .andExpect(jsonPath("$[0].id").value(10))
                    .andExpect(jsonPath("$[0].nombre").value("Carlos"))
                    .andExpect(jsonPath("$[0].usuarioId").value(1));
        }

        @Test
        @DisplayName("retorna 200 OK con lista vacía cuando el usuario no tiene contactos")
        void listar_vacio_200() throws Exception {
            when(contactoService.listarContactosPorUsuario(1L)).thenReturn(List.of());

            mockMvc.perform(get("/api/contacto/usuario/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(0));
        }

        @Test
        @DisplayName("retorna 200 OK con múltiples contactos")
        void listar_multiples_200() throws Exception {
            Contacto c2 = new Contacto();
            c2.setId(11L);
            c2.setNombre("Lucia");
            c2.setApellido("Fernandez");
            c2.setUsuario(usuarioBase());

            when(contactoService.listarContactosPorUsuario(1L)).thenReturn(List.of(contactoBase(), c2));

            mockMvc.perform(get("/api/contacto/usuario/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[1].nombre").value("Lucia"));
        }
    }
}
