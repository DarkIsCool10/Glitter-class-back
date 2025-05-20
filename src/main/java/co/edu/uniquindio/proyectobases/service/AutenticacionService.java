package co.edu.uniquindio.proyectobases.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.proyectobases.dto.AutenticacionDto.LoginResponseDto;
import co.edu.uniquindio.proyectobases.repository.AutenticacionRepository;

import java.util.Optional;

/**
 * Servicio encargado de la lógica de autenticación de usuarios.
 * Interactúa con el repositorio de autenticación para validar credenciales y retornar información de login.
 */
@Service
public class AutenticacionService {
    /**
     * Repositorio que gestiona la validación de credenciales de usuario.
     */
    @Autowired
    private AutenticacionRepository autenticacionRepository;

    /**
     * Valida las credenciales de un usuario y retorna la información correspondiente si el login es exitoso.
     *
     * @param correo correo electrónico del usuario
     * @param contrasena contraseña del usuario
     * @return Optional con la respuesta de login si las credenciales son correctas, vacío en caso contrario
     */
    public Optional<LoginResponseDto> login(String correo, String contrasena) {
        return autenticacionRepository.validarLogin(correo, contrasena);
    }
}
