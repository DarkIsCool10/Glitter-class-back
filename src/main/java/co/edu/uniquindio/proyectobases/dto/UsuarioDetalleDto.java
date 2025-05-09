package co.edu.uniquindio.proyectobases.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public record UsuarioDetalleDto(
    Long idUsuario,
    String nombre,
    String apellido,
    String correo,
    Long idUnidad,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime fechaRegistro,
    String estado,
    Long idRol
) {}
