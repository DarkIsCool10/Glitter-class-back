package co.edu.uniquindio.proyectobases.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.ObtenerPreguntaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.OpcionRespuestaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaConOpcionesDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.OpcionRespuestaCreadaDto;
import co.edu.uniquindio.proyectobases.repository.PreguntaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PreguntaService {

    @Autowired
    private PreguntaRepository preguntaRepository;

    

    public MensajeDto<Long> crearPregunta(PreguntaDto dto) {
        Optional<Long> idPregunta = preguntaRepository.crearPregunta(dto);
        if (idPregunta.isPresent()) {
            return new MensajeDto<>(false, idPregunta.get());
        } else {
            return new MensajeDto<>(true, null);
        }
    }

    public MensajeDto<OpcionRespuestaCreadaDto> crearOpcion(Long idPregunta, OpcionRespuestaDto dto) {
    OpcionRespuestaCreadaDto respuesta = preguntaRepository.crearOpcionRespuesta(idPregunta, dto);
    if (respuesta != null && respuesta.resultado() == 1) {
        return new MensajeDto<>(false, respuesta);
    } else {
        return new MensajeDto<>(true, null);
    }
}

    public List<PreguntaConOpcionesDto> obtenerTodasLasPreguntasConOpciones() {
        return preguntaRepository.obtenerTodasLasPreguntasConOpciones();
    }

    public MensajeDto<List<ObtenerPreguntaDto>> obtenerPreguntasDocente(Long idUsuario) {
        return new MensajeDto<>(false, preguntaRepository.listarPreguntasDocente(idUsuario));
    }
}

