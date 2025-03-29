package co.edu.uniquindio.proyectobases;

import co.edu.uniquindio.proyectobases.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication

public class ProyectoBasesApplication implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public static void main(String[] args) {
        SpringApplication.run(ProyectoBasesApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Conectado a Oracle. Número de usuarios: " + usuarioRepository.count());
    }
}
