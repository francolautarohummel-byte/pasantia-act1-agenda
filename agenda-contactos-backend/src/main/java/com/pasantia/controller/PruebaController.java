package com.pasantia.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping

public class PruebaController {

    @GetMapping
    public String Saludar() {
        return "Hola Mundo";
    }

}
