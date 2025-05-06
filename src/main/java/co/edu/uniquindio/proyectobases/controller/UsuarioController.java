package co.edu.uniquindio.proyectobases.controller;

import co.edu.uniquindio.proyectobases.dto.UsuarioDto;
import co.edu.uniquindio.proyectobases.repository.UsuarioRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    public List<UsuarioDto> obtenerTodosUsuarios() {
        return usuarioRepository.obtenerTodosUsuarios();
    }
}