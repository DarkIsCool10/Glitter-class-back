package co.edu.uniquindio.proyectobases.service;

import org.springframework.stereotype.Service;
import co.edu.uniquindio.proyectobases.repository.UsuarioRepository;
import co.edu.uniquindio.proyectobases.dto.MensajeDto;

@Service
public class PublicoService {

    private final UsuarioRepository usuarioRepository;

    public PublicoService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public MensajeDto<?> obtenerUsuario(Long idUsuario) {
        return usuarioRepository.obtenerUsuarioPorId(idUsuario)
            .<MensajeDto<?>>map(usuario -> new MensajeDto<>(false, usuario))
            .orElseGet(() -> new MensajeDto<>(true, "Usuario no encontrado"));
    }
}
