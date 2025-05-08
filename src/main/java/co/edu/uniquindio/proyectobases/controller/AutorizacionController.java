package co.edu.uniquindio.proyectobases.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.uniquindio.proyectobases.dto.LoginDto;
import co.edu.uniquindio.proyectobases.dto.LoginResponseDto;
import co.edu.uniquindio.proyectobases.model.Credenciales;
import co.edu.uniquindio.proyectobases.service.LoginService;

import java.util.Optional;

@RestController
@RequestMapping("/api/autorizacion")
@CrossOrigin(origins = {"http://localhost:4200", "*"})
public class AutorizacionController {

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto logindto) {
        Optional<Credenciales> cred = loginService.login(logindto.correo(), logindto.contrasena());
        if (cred.isPresent()) {
            Credenciales c = cred.get();
            return ResponseEntity.ok(
                new LoginResponseDto(c.getIdUsuario(), c.getIdRol(), c.getCorreo())
            );
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }
}
