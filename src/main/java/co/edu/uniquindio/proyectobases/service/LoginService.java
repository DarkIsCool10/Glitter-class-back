package co.edu.uniquindio.proyectobases.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.uniquindio.proyectobases.dto.LoginResponseDto;
import co.edu.uniquindio.proyectobases.repository.CredencialesRepository;

import java.util.Optional;

@Service
public class LoginService {
    @Autowired
    private CredencialesRepository credencialesRepository;

    public Optional<LoginResponseDto> login(String correo, String contrasena) {
        return credencialesRepository.validarLogin(correo, contrasena);
    }
}
