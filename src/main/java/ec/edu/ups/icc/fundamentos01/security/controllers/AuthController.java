package ec.edu.ups.icc.fundamentos01.security.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ec.edu.ups.icc.fundamentos01.security.dtos.AuthResponseDto;
import ec.edu.ups.icc.fundamentos01.security.dtos.LoginRequestDto;
import ec.edu.ups.icc.fundamentos01.security.dtos.RefreshTokenRequestDto;
import ec.edu.ups.icc.fundamentos01.security.dtos.RegisterRequestDto;
import ec.edu.ups.icc.fundamentos01.security.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(
    name = "Autenticacion",
    description = "Endpoints publicos para registro e inicio de sesion"
)
@SecurityRequirements
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Login - Endpoint público (configurado en SecurityConfig)
     * POST /auth/login
     */
    @Operation(
        summary = "Iniciar sesión",
        description = "Valida las credenciales, revoca refresh tokens anteriores del usuario y devuelve un access token y un refresh token nuevos"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Login exitoso"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos invalidos"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Credenciales incorrectas"
        )
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        AuthResponseDto response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    /**
     * Registro - Endpoint público (configurado en SecurityConfig)
     * POST /auth/register
     */
    @Operation(
        summary = "Registrar usuario",
        description = "Crea un nuevo usuario, asigna Role_USER y devuelve JWT"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Usuario registrado correctamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos invalidos"
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Email ya registrado"
        )
    })
    
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDto> register(@Valid @RequestBody RegisterRequestDto registerRequest) {
        AuthResponseDto response = authService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Refresh - Recibe un refresh token válido y devuelve nuevos tokens.
     * POST /auth/refresh
     */
    @Operation(
        summary = "Renovar tokens",
        description = "Valida el refresh token recibido, lo revoca y devuelve un access token y un refresh token nuevos (rotación de tokens)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Tokens renovados correctamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Refresh token invalido, expirado, revocado o usuario no valido"
        )
    })
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponseDto> refresh(@Valid @RequestBody RefreshTokenRequestDto request) {
        AuthResponseDto response = authService.refresh(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Logout - Revoca el refresh token recibido.
     * POST /auth/logout
     */
    @Operation(
        summary = "Cerrar sesión",
        description = "Revoca el refresh token recibido, invalidando la sesión asociada"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Sesión cerrada correctamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Refresh token invalido, expirado o ya revocado"
        )
    })
    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@Valid @RequestBody RefreshTokenRequestDto request) {
        authService.logout(request);
    }
}
