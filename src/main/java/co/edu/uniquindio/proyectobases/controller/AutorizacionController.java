package co.edu.uniquindio.proyectobases.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.uniquindio.proyectobases.dto.LoginDto;
import co.edu.uniquindio.proyectobases.dto.LoginResponseDto;
import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.model.Credenciales;
import co.edu.uniquindio.proyectobases.service.LoginService;

import java.util.Optional;

@RestController
@RequestMapping("/api/autorizacion")
@CrossOrigin(origins = {"http://localhost:4200", "*"})
public class AutorizacionController {

    private final LoginService loginService;

    public AutorizacionController(LoginService loginService){
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<MensajeDto<LoginResponseDto>> login(@RequestBody LoginDto logindto) {
        try {
            Optional<Credenciales> cred = loginService.login(logindto.correo(), logindto.contrasena());
            if (cred.isPresent()) {
                Credenciales c = cred.get();
                LoginResponseDto loginResponse = new LoginResponseDto(c.getIdUsuario(), c.getIdRol(), c.getCorreo());
                MensajeDto<LoginResponseDto> respuesta = new MensajeDto<>(false, loginResponse);
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
