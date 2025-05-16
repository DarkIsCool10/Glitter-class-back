package co.edu.uniquindio.proyectobases.service;

import org.springframework.stereotype.Service;
import co.edu.uniquindio.proyectobases.repository.ExamenRepository;
import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.ExamenDto;

import java.util.Optional;

@Service
public class ExamenService {

    private final ExamenRepository examenRepository;

    public ExamenService(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    public MensajeDto<Long> crearExamen(ExamenDto dto) {
        try {
            Optional<Long> id = examenRepository.crearExamen(dto);
            return id
                .map(i -> new MensajeDto<>(false, i))
                .orElseGet(() -> new MensajeDto<>(true, null));
        } catch (Exception e) {
            return new MensajeDto<>(true, null);
        }
    }
}
