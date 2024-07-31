package org.mju_likelion.festival.auth.controller;

import lombok.AllArgsConstructor;
import org.mju_likelion.festival.auth.dto.response.KeyResponse;
import org.mju_likelion.festival.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

  private final AuthService authService;

  @GetMapping("/auth/key")
  public ResponseEntity<KeyResponse> getKey() {
    return ResponseEntity.ok(authService.getKey());
  }
}
