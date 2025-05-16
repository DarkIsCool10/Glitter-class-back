package co.edu.uniquindio.proyectobases.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.OpcionRespuestaDto;
import co.edu.uniquindio.proyectobases.dto.PreguntaDto.PreguntaDto;
import co.edu.uniquindio.proyectobases.repository.PreguntaRepository;
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

    public MensajeDto<Integer> crearOpcion(OpcionRespuestaDto dto) {
        int resultado = preguntaRepository.crearOpcion(dto);
        if (resultado == 1) {
            return new MensajeDto<>(false, resultado);
        } else {
            return new MensajeDto<>(true, null);
        }
    }
}

