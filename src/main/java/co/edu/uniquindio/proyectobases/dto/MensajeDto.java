package co.edu.uniquindio.proyectobases.dto;

public record MensajeDto<T>(
        boolean error,
        T respuesta
) {
}
