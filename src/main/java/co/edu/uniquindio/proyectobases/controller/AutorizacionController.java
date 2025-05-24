package co.edu.uniquindio.proyectobases.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.dto.AutenticacionDto.LoginDto;
import co.edu.uniquindio.proyectobases.dto.AutenticacionDto.LoginResponseDto;
import co.edu.uniquindio.proyectobases.exception.AutorizacionException;
import co.edu.uniquindio.proyectobases.service.AutenticacionService;

/**
 * Controlador que gestiona las operaciones de autenticación.
 */
@RestController
@RequestMapping("/api/autorizacion")
@CrossOrigin(origins = {"http://localhost:4200", "*"})
public class AutorizacionController {
    
    /**
     * Servicio que gestiona las operaciones de autenticación.
     */
    private final AutenticacionService autenticacionService;

    /**
     * Constructor que inicializa el servicio.
     * @param autenticacionService servicio que gestiona las operaciones de autenticación
     */
    public AutorizacionController(AutenticacionService autenticacionService){
        this.autenticacionService = autenticacionService;
    }

    /**
     * Realiza el login de un usuario.
     * @param logindto DTO con los datos del usuario
     * @return ResponseEntity con el mensaje de respuesta
     * @throws AutorizacionException si ocurre un error al realizar el login
     */
    @PostMapping("/login")
    public ResponseEntity<MensajeDto<LoginResponseDto>> login(@RequestBody LoginDto logindto) throws AutorizacionException {
        try {
            LoginResponseDto loginResponseDto = autenticacionService.login(logindto);
            return ResponseEntity.ok(new MensajeDto<>(false, "Login exitoso", loginResponseDto));
        } catch (AutorizacionException e) {
            return ResponseEntity.badRequest().body(new MensajeDto<>(true, "Credenciales inválidas", null));
        }
    }
}
