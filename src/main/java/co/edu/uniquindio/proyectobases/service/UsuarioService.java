package co.edu.uniquindio.proyectobases.service;

import co.edu.uniquindio.proyectobases.model.Usuario;
import co.edu.uniquindio.proyectobases.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}

