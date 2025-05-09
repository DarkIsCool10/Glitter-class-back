package co.edu.uniquindio.proyectobases.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.uniquindio.proyectobases.dto.MensajeDto;
import co.edu.uniquindio.proyectobases.service.PublicoService;

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = {"http://localhost:4200", "*"})
public class PublicoController {

    private final PublicoService publicoService;

    public PublicoController(PublicoService publicoService) {
        this.publicoService = publicoService;
    }

    @GetMapping("/obtener-usuario/{id}")
    public ResponseEntity<MensajeDto<?>> obtenerUsuario(@PathVariable Long id) {
        MensajeDto<?> respuesta = publicoService.obtenerUsuario(id);
        return ResponseEntity.status(respuesta.error() ? 404 : 200).body(respuesta);
    }
}
