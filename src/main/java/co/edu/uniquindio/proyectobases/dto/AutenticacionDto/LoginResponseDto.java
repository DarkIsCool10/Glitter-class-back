package co.edu.uniquindio.proyectobases.dto.AutenticacionDto;

public record LoginResponseDto(
    Long idUsuario, 
    Integer idRol, 
    String correo
) {}