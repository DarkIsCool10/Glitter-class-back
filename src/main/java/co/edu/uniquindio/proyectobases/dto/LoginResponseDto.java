package co.edu.uniquindio.proyectobases.dto;

public record LoginResponseDto(
    Long idUsuario, 
    Integer idRol, 
    String correo
) {}