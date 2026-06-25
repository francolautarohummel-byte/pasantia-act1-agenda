package com.pasantia.service;

import com.pasantia.entity.Contacto;
import java.util.List;

public interface ContactoService {
    Contacto guardarContacto(Contacto contacto, Long usuarioId);
    List<Contacto> listarContactosPorUsuario(Long usuarioId);
    Contacto actualizarContacto(Long contactoId, Contacto contactoDetalles);
    void eliminarContacto(Long contactoId);
    List<Contacto> buscarContactos(Long usuarioId, String texto);
}
