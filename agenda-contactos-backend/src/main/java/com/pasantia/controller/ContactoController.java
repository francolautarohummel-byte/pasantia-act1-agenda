package com.pasantia.controller;

import com.pasantia.dto.ContactoRequestDTO;
import com.pasantia.dto.ContactoResponseDTO;
import com.pasantia.entity.Contacto;
import com.pasantia.service.ContactoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contacto")
@RequiredArgsConstructor
public class ContactoController {

    private final ContactoService contactoService;

    @PostMapping
    public ResponseEntity<?> crearContacto(@RequestBody ContactoRequestDTO requestDTO, @RequestParam Long usuarioId) {
        try {
            Contacto contacto = new Contacto();
            contacto.setNombre(requestDTO.getNombre());
            contacto.setApellido(requestDTO.getApellido());
            contacto.setTelefono(requestDTO.getTelefono());
            contacto.setEmail(requestDTO.getEmail());
            contacto.setDireccion(requestDTO.getDireccion());
            contacto.setObservaciones(requestDTO.getObservaciones());

            Contacto nuevoContacto = contactoService.guardarContacto(contacto, usuarioId);

            return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponseDTO(nuevoContacto));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<ContactoResponseDTO>> listarContactos(@PathVariable Long usuarioId) {
        List<Contacto> contactos = contactoService.listarContactosPorUsuario(usuarioId);
        
        List<ContactoResponseDTO> dtos = contactos.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
                
        return ResponseEntity.ok(dtos);
    }

    // Método helper para convertir Entidad -> DTO
    private ContactoResponseDTO convertToResponseDTO(Contacto contacto) {
        ContactoResponseDTO dto = new ContactoResponseDTO();
        dto.setId(contacto.getId());
        dto.setNombre(contacto.getNombre());
        dto.setApellido(contacto.getApellido());
        dto.setTelefono(contacto.getTelefono());
        dto.setEmail(contacto.getEmail());
        dto.setDireccion(contacto.getDireccion());
        dto.setObservaciones(contacto.getObservaciones());
        
        if (contacto.getUsuario() != null) {
            dto.setUsuarioId(contacto.getUsuario().getId());
        }
        return dto;
    }
}
