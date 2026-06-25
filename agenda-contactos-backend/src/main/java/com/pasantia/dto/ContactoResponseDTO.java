package com.pasantia.dto;

import lombok.Data;

@Data
public class ContactoResponseDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String direccion;
    private String observaciones;
    private Long usuarioId; // Solo el ID plano
}