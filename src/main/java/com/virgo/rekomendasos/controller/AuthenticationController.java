package com.virgo.rekomendasos.controller;

import com.virgo.rekomendasos.utils.dto.AuthenticationRequestDTO;
import com.virgo.rekomendasos.utils.dto.AuthenticationResponseDTO;
import com.virgo.rekomendasos.utils.dto.RegisterRequestDTO;
import com.virgo.rekomendasos.service.AuthenticationService;
import com.virgo.rekomendasos.utils.response.WebResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RestControllerAdvice
@RequiredArgsConstructor
@ApiResponses({
        @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "403", content = { @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
})
@Tag(name = "Auth", description = "Auth management APIs")
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequestDTO request) {
        return new ResponseEntity(new WebResponse("Berhasil Register", HttpStatus.OK, service.register(request)), HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO request) {
        return new ResponseEntity(new WebResponse("Login Successfully", HttpStatus.OK, service.authenticate(request)), HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        service.refreshToken(request, response);
    }

}
