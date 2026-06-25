package com.pasantia.repository; // O el paquete donde tengas este archivo

import com.pasantia.entity.Contacto; // 👈 IMPORT CORREGIDO
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;


public interface ContactoRepository extends JpaRepository<Contacto, Long> {

    // En JPQL usamos el nombre de la Entidad (Contacto). 
    // Como está importada arriba, la consulta funciona sin cambios.
    @Query("SELECT c FROM Contacto c WHERE c.usuario.id = :usuarioId ORDER BY c.nombre ASC")
    List<Contacto> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT c FROM Contacto c WHERE c.usuario.id = :usuarioId AND " +
           "(LOWER(c.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(c.apellido) LIKE LOWER(CONCAT('%', :texto, '%')))")
    List<Contacto> buscarPorNombreOApellido(@Param("usuarioId") Long usuarioId, @Param("texto") String texto);

    @Query(value = "SELECT * FROM contacto WHERE usuario_id = :usuarioId AND telefono = :telefono", nativeQuery = true)
    List<Contacto> buscarPorTelefonoNativo(@Param("usuarioId") Long usuarioId, @Param("telefono") String telefono);
}
