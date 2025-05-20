package co.edu.uniquindio.proyectobases.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.AutenticacionDto.LoginDto;
import co.edu.uniquindio.proyectobases.dto.AutenticacionDto.LoginResponseDto;
import co.edu.uniquindio.proyectobases.exception.AutorizacionException;
import co.edu.uniquindio.proyectobases.service.AutenticacionService;

@RestController
@RequestMapping("/api/autorizacion")
@CrossOrigin(origins = {"http://localhost:4200", "*"})
public class AutorizacionController {

    private final AutenticacionService autenticacionService;

    public AutorizacionController(AutenticacionService autenticacionService){
        this.autenticacionService = autenticacionService;
    }

    @PostMapping("/login")
    public ResponseEntity<MensajeDto<LoginResponseDto>> login(@RequestBody LoginDto logindto) throws AutorizacionException {
        try {
            LoginResponseDto loginResponseDto = autenticacionService.login(logindto);
            return ResponseEntity.ok(new MensajeDto<>(false, "Login exitoso", loginResponseDto));
        } catch (AutorizacionException e) {
            return ResponseEntity.badRequest().body(new MensajeDto<>(true, e.getMessage(), null));
        }
    }
}
