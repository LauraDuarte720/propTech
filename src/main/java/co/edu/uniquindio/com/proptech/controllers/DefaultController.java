package co.edu.uniquindio.com.proptech.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DefaultController {
    @GetMapping
    public String defaultController(){
        return "Hello this is an example";
    }
}
