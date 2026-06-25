package com.pasantia.service.impl;

import com.pasantia.entity.Contacto;
import com.pasantia.entity.Usuario;
import com.pasantia.repository.ContactoRepository;
import com.pasantia.repository.UsuarioRepository;
import com.pasantia.service.ContactoService;
import lombok.RequiredArgsConstructor; // 👈 NUEVO IMPORT
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor // 👈 REEMPLAZA EL AUTOWIRED
public class ContactoServiceImpl implements ContactoService {

    // Cambiados a 'private final' sin @Autowired
    private final ContactoRepository contactoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public Contacto guardarContacto(Contacto contacto, Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + usuarioId));

        contacto.setUsuario(usuario);
        return contactoRepository.save(contacto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contacto> listarContactosPorUsuario(Long usuarioId) {
        return contactoRepository.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional
    public Contacto actualizarContacto(Long contactoId, Contacto contactoDetalles) {
        Contacto contactoExistente = contactoRepository.findById(contactoId)
                .orElseThrow(() -> new RuntimeException("Contacto no encontrado con ID: " + contactoId));

        contactoExistente.setNombre(contactoDetalles.getNombre());
        contactoExistente.setApellido(contactoDetalles.getApellido());
        contactoExistente.setTelefono(contactoDetalles.getTelefono());
        contactoExistente.setEmail(contactoDetalles.getEmail());
        contactoExistente.setDireccion(contactoDetalles.getDireccion());
        contactoExistente.setObservaciones(contactoDetalles.getObservaciones());

        return contactoRepository.save(contactoExistente);
    }

    @Override
    @Transactional
    public void eliminarContacto(Long contactoId) {
        Contacto contacto = contactoRepository.findById(contactoId)
                .orElseThrow(() -> new RuntimeException("Contacto no encontrado con ID: " + contactoId));
        contactoRepository.delete(contacto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Contacto> buscarContactos(Long usuarioId, String texto) {
        return contactoRepository.buscarPorNombreOApellido(usuarioId, texto);
    }
}
