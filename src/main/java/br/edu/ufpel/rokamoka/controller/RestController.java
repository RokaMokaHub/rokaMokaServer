package br.edu.ufpel.rokamoka.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/teste")
public class RestController {

    @GetMapping
    public ResponseEntity<String> getUser() {
        return ResponseEntity.ok("Autenticado");
    }
}
