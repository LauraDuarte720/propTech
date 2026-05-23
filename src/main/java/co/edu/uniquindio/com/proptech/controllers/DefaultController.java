package co.edu.uniquindio.com.proptech.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class DefaultController {
    @GetMapping("/")
    public String home() {
        return "redirect:/proptech_login.html";
    }
}
