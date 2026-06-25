package com.pasantia.dto;

import lombok.Data;

@Data
public class ContactoRequestDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    private String email;
    private String direccion;
    private String observaciones;
}