package co.edu.uniquindio.proyectobases.service;

import java.util.List;

import org.springframework.stereotype.Service;
import co.edu.uniquindio.proyectobases.repository.PublicoRepository;
import co.edu.uniquindio.proyectobases.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.CursoDto.CursoDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.ExamenResumenDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.DificultadDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.TemaDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.TipoPreguntaDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.VisibilidadDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaDocenteDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaPublicaDto;

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

    public MensajeDto<List<TemaDto>> obtenerTemas(){
        List<TemaDto> temas = publicoRepository.listarTemas();
        return new MensajeDto<>(false, temas);
    }

    // public MensajeDto<List<CursoDto>> obtenerCursos() {
    //     List<CursoDto> cursos = publicoRepository.listarCursos();
    //     return new MensajeDto<>(false, cursos);
    // }

    public MensajeDto<List<DificultadDto>> obtenerDificultades() {
        List<DificultadDto> dificultad = publicoRepository.listarDificultades();
        return new MensajeDto<>(false, dificultad);
    }

    public MensajeDto<List<TipoPreguntaDto>> obtenerTipos() {
        List<TipoPreguntaDto> tipo = publicoRepository.listarTipos();
        return new MensajeDto<>(false, tipo);
    }

    public MensajeDto<List<CursoDto>> obtenerCursosDocente(Long idUsuario) {
        return new MensajeDto<>(false, publicoRepository.listarCursosDocente(idUsuario));
    }

    public MensajeDto<List<CursoDto>> obtenerCursosEstudiante(Long idUsuario) {
        return new MensajeDto<>(false, publicoRepository.listarCursosEstudiante(idUsuario));
    }
    
    public MensajeDto<List<PreguntaPublicaDto>> obtenerPreguntasPublicas() {
        return new MensajeDto<>(false, publicoRepository.listarPreguntasPublicas());
    }

    public MensajeDto<List<VisibilidadDto>> obtenerVisibilidades() {
        List<VisibilidadDto> visibilidad = publicoRepository.listarVisibilidades();
        return new MensajeDto<>(false, visibilidad);
    }

    public MensajeDto<List<ExamenResumenDto>> obtenerExamenes() {
        List<ExamenResumenDto> examenes = publicoRepository.listarExamenes();
        return new MensajeDto<>(false, examenes);
    }

    public MensajeDto<List<PreguntaDocenteDto>> obtenerPreguntasDocente(Long idUsuario) {
        return new MensajeDto<>(false, publicoRepository.findPreguntasByDocente(idUsuario));
    }

}
