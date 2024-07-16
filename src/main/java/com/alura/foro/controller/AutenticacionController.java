package com.alura.foro.controller;

import com.alura.foro.model.DatosAutenticacion;
import com.alura.foro.model.DatosTokenJWT;
import com.alura.foro.model.Usuario;
import com.alura.foro.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;


    @PostMapping
    public ResponseEntity  realizarLogin(@RequestBody @Valid DatosAutenticacion datos) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(datos.login(), datos.contrasena());
        var authentication = manager.authenticate(authenticationToken);

        var tokenJWT = tokenService.generarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DatosTokenJWT(tokenJWT));
    }
}
