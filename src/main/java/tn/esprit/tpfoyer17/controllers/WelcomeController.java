package tn.esprit.tpfoyer17.controllers;

import org.springframework.web.bind.annotation.*;


@RestController
public class WelcomeController {


    @GetMapping(value = "/")
    public String welcome() { return "From Docker Container: Bonjour, Bienvenue à l'application de test des Web Services REST."; }


}

