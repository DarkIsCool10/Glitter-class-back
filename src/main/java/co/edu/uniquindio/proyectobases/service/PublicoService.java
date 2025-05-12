package co.edu.uniquindio.proyectobases.service;

import java.util.List;

import org.springframework.stereotype.Service;
import co.edu.uniquindio.proyectobases.repository.PublicoRepository;
import co.edu.uniquindio.proyectobases.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import co.edu.uniquindio.proyectobases.dto.CategoriaDto;
import co.edu.uniquindio.proyectobases.dto.CursoDto;
import co.edu.uniquindio.proyectobases.dto.DificultadDto;
import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.TipoPreguntaDto;

@Service
@AllArgsConstructor
public class PublicoService {

    private final UsuarioRepository usuarioRepository;
    private final PublicoRepository publicoRepository;


    public MensajeDto<?> obtenerUsuario(Long idUsuario) {
        return usuarioRepository.obtenerUsuarioPorId(idUsuario)
            .<MensajeDto<?>>map(usuario -> new MensajeDto<>(false, usuario))
            .orElseGet(() -> new MensajeDto<>(true, "Usuario no encontrado"));
    }

    public MensajeDto<List<CategoriaDto>> obtenerCategorias(){
        List<CategoriaDto> categorias = publicoRepository.listarCategorias();
        return new MensajeDto<List<CategoriaDto>>(false, categorias);
    }

    public MensajeDto<List<CursoDto>> obtenerCursos() {
        List<CursoDto> cursos = publicoRepository.listarCursos();
        return new MensajeDto<>(false, cursos);
    }

    public MensajeDto<List<DificultadDto>> obtenerDificultades() {
        List<DificultadDto> dificultad = publicoRepository.listarDificultades();
        return new MensajeDto<>(false, dificultad);
    }

    public MensajeDto<List<TipoPreguntaDto>> obtenerTipos() {
        List<TipoPreguntaDto> tipo = publicoRepository.listarTipos();
        return new MensajeDto<>(false, tipo);
    }
    
}
