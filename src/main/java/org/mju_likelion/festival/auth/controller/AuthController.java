package org.mju_likelion.festival.auth.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.auth.domain.RsaKeyStrategy;
import org.mju_likelion.festival.auth.dto.request.UserLoginRequest;
import org.mju_likelion.festival.auth.dto.response.KeyResponse;
import org.mju_likelion.festival.auth.dto.response.LoginResponse;
import org.mju_likelion.festival.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

  private final AuthService authService;

  @GetMapping("/auth/key")
  public ResponseEntity<KeyResponse> getKey() {
    return ResponseEntity.ok(authService.getKey());
  }

  @PostMapping("/auth/user/login")
  public ResponseEntity<LoginResponse> login(@RequestBody @Valid UserLoginRequest userLoginRequest,
      @RequestParam RsaKeyStrategy rsaKeyStrategy) {
    return ResponseEntity.ok(authService.userLogin(userLoginRequest, rsaKeyStrategy));
  }
}
