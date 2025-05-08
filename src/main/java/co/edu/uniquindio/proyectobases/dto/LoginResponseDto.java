package co.edu.uniquindio.proyectobases.dto;

public record LoginResponseDto(
    Long idUsuario, 
    Long idRol, 
    String correo
) {}