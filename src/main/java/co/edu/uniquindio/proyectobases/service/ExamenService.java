package co.edu.uniquindio.proyectobases.service;

import org.springframework.stereotype.Service;
import co.edu.uniquindio.proyectobases.repository.ExamenRepository;
import co.edu.uniquindio.proyectobases.dto.ExamenDto.CrearExamenDto;
import co.edu.uniquindio.proyectobases.exception.ExamenException;

import java.util.Optional;

/**
 * Servicio encargado de la lógica de negocio relacionada con exámenes.
 * Permite crear exámenes a través del repositorio y retorna los resultados en objetos MensajeDto.
 */
@Service
public class ExamenService {

    /**
     * Repositorio que gestiona la persistencia y consulta de exámenes en la base de datos.
     */
    private final ExamenRepository examenRepository;

    /**
     * Constructor con inyección de dependencias.
     * @param examenRepository repositorio de exámenes
     */
    public ExamenService(ExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    /**
     * Crea un nuevo examen en la base de datos utilizando los datos proporcionados en el DTO.
     * Si la creación es exitosa, retorna el id del examen creado; en caso de error, retorna null y marca el mensaje como error.
     *
     * @param dto DTO con los datos necesarios para crear el examen
     * @return MensajeDto con el id del examen creado o null si hubo error
     */
    public CrearExamenDto crearExamen(CrearExamenDto dto) throws ExamenException {
        try{
            Optional<Long> idExamen = examenRepository.crearExamen(dto);
            if (idExamen.isPresent()) {
                throw new ExamenException("Examen creado exitosamente");
            } else {
                throw new ExamenException("Error al crear el examen");
            }
        }catch(ExamenException e){
            throw new ExamenException(e.getMessage());
        }
    }

}
