package co.edu.uniquindio.proyectobases.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.AutenticacionDto.LoginDto;
import co.edu.uniquindio.proyectobases.dto.AutenticacionDto.LoginResponseDto;
import co.edu.uniquindio.proyectobases.service.AutenticacionService;

import java.util.Optional;

@RestController
@RequestMapping("/api/autorizacion")
@CrossOrigin(origins = {"http://localhost:4200", "*"})
public class AutorizacionController {

    private final AutenticacionService autenticacionService;

    public AutorizacionController(AutenticacionService autenticacionService){
        this.autenticacionService = autenticacionService;
    }

    @PostMapping("/login")
    public ResponseEntity<MensajeDto<LoginResponseDto>> login(@RequestBody LoginDto logindto) {
        try {
            Optional<LoginResponseDto> loginResponse = autenticacionService.login(logindto.correo(), logindto.contrasena());
            if (loginResponse.isPresent()) {
                MensajeDto<LoginResponseDto> respuesta = new MensajeDto<>(false, loginResponse.get());
                return ResponseEntity.status(200).body(respuesta);
            } else {
                MensajeDto<LoginResponseDto> respuesta = new MensajeDto<>(true, null);
                return ResponseEntity.status(401).body(respuesta);
            }
        } catch (Exception e) {
            MensajeDto<LoginResponseDto> respuesta = new MensajeDto<>(true, null);
            return ResponseEntity.status(500).body(respuesta);
        }
    }
}
