package co.edu.uniquindio.proyectobases.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.uniquindio.proyectobases.dto.LoginDto;
import co.edu.uniquindio.proyectobases.model.Credenciales;
import co.edu.uniquindio.proyectobases.service.LoginService;

import java.util.Optional;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginDto logindto) {
        Optional<Credenciales> cred = loginService.login(logindto.correo(), logindto.contrasena());
        if (cred.isPresent()) {
            Credenciales c = cred.get();
            return ResponseEntity.ok(
                new Object() {
                    public final Long idUsuario = c.getIdUsuario();
                    public final Long idRol = c.getIdRol();
                    public final String correo = c.getCorreo();
                }
            );
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }
}
