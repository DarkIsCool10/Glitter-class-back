package co.edu.uniquindio.proyectobases.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import co.edu.uniquindio.proyectobases.repository.PublicoRepository;
import lombok.AllArgsConstructor;
import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.CursosDto.CursoDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.ExamenResumenDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.DificultadDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.TemaDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.TipoPreguntaDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.UnidadAcademicaDto;
import co.edu.uniquindio.proyectobases.dto.ParametricasDto.VisibilidadDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.ObtenerPreguntaDto;
import co.edu.uniquindio.proyectobases.dto.UsuarioDto.UsuarioDetalleDto;

@Service
@AllArgsConstructor
public class PublicoService {

    private final PublicoRepository publicoRepository;


    public MensajeDto<UsuarioDetalleDto> obtenerUsuario(Long idUsuario) {
        Optional<UsuarioDetalleDto> usuario = publicoRepository.obtenerUsuarioPorId(idUsuario);
        return new MensajeDto<>(false, usuario.get());
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
    
    public MensajeDto<List<ObtenerPreguntaDto>> obtenerPreguntasPublicas() {
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

    public MensajeDto<List<UnidadAcademicaDto>> obtenerUnidades() {
        List<UnidadAcademicaDto> unidades = publicoRepository.listarUnidades();
        return new MensajeDto<>(false, unidades);
    }

    public MensajeDto<List<UnidadAcademicaDto>> obtenerUnidadesDocente(Long idUsuario) {
        List<UnidadAcademicaDto> unidades = publicoRepository.listarUnidadesDocente(idUsuario);
        return new MensajeDto<>(false, unidades);
    }

    public MensajeDto<List<TemaDto>> obtenerTemasUnidad(Long idUnidad) {
        List<TemaDto> temas = publicoRepository.listarTemasUnidad(idUnidad);
        return new MensajeDto<>(false, temas);
    }

}
