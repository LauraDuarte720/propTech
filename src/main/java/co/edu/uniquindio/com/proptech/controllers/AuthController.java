package co.edu.uniquindio.com.proptech.controllers;

import co.edu.uniquindio.com.proptech.domain.dtos.LoginRequestDto;
import co.edu.uniquindio.com.proptech.domain.dtos.LoginResponseDto;
import co.edu.uniquindio.com.proptech.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Validated @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
}