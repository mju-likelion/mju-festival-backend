package org.mju_likelion.festival.auth.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.auth.domain.RsaKeyStrategy;
import org.mju_likelion.festival.auth.dto.request.AdminLoginRequest;
import org.mju_likelion.festival.auth.dto.request.UserLoginRequest;
import org.mju_likelion.festival.auth.dto.response.KeyResponse;
import org.mju_likelion.festival.auth.dto.response.LoginResponse;
import org.mju_likelion.festival.auth.dto.response.TermResponse;
import org.mju_likelion.festival.auth.service.AuthQueryService;
import org.mju_likelion.festival.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

  private final AuthService authService;
  private final AuthQueryService authQueryService;

  @GetMapping("/key")
  public ResponseEntity<KeyResponse> getKey() {
    return ResponseEntity.ok(authQueryService.getKey());
  }

  @PostMapping("/user/login")
  public ResponseEntity<LoginResponse> userLogin(
      @RequestBody @Valid UserLoginRequest userLoginRequest,
      @RequestParam RsaKeyStrategy rsaKeyStrategy) {
    return ResponseEntity.ok(authService.userLogin(userLoginRequest, rsaKeyStrategy));
  }

  @PostMapping("/admin/login")
  public ResponseEntity<LoginResponse> adminLogin(
      @RequestBody @Valid AdminLoginRequest adminLoginRequest,
      @RequestParam RsaKeyStrategy rsaKeyStrategy) {
    return ResponseEntity.ok(authQueryService.adminLogin(adminLoginRequest, rsaKeyStrategy));
  }

  @GetMapping("/terms")
  public ResponseEntity<List<TermResponse>> getTerms() {
    return ResponseEntity.ok(authQueryService.getTerms());
  }
}
