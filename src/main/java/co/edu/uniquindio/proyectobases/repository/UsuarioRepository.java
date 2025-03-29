package co.edu.uniquindio.proyectobases.repository;

import co.edu.uniquindio.proyectobases.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
}

