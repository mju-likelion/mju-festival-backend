package org.mju_likelion.festival.auth.controller;

import static org.mju_likelion.festival.common.api.ApiPaths.ADMIN_LOGIN;
import static org.mju_likelion.festival.common.api.ApiPaths.AUTH_KEY;
import static org.mju_likelion.festival.common.api.ApiPaths.USER_LOGIN;
import static org.mju_likelion.festival.common.api.ApiPaths.USER_WITHDRAW;

import jakarta.validation.Valid;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.auth.dto.request.AdminLoginRequest;
import org.mju_likelion.festival.auth.dto.request.UserLoginRequest;
import org.mju_likelion.festival.auth.dto.response.AdminLoginResponse;
import org.mju_likelion.festival.auth.dto.response.KeyResponse;
import org.mju_likelion.festival.auth.dto.response.UserLoginResponse;
import org.mju_likelion.festival.auth.service.AuthQueryService;
import org.mju_likelion.festival.auth.service.AuthService;
import org.mju_likelion.festival.auth.util.rsa_key.RsaKeyStrategy;
import org.mju_likelion.festival.common.authentication.AuthenticationPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final AuthQueryService authQueryService;

  @GetMapping(AUTH_KEY)
  public ResponseEntity<KeyResponse> getKey() {

    return ResponseEntity.ok(authQueryService.getKey());
  }

  @PostMapping(USER_LOGIN)
  public ResponseEntity<UserLoginResponse> userLogin(
      @RequestBody @Valid final UserLoginRequest userLoginRequest,
      @RequestParam final String rsaKeyStrategy) {

    return ResponseEntity.ok(
        authService.userLogin(userLoginRequest, RsaKeyStrategy.fromString(rsaKeyStrategy)));
  }

  @PostMapping(ADMIN_LOGIN)
  public ResponseEntity<AdminLoginResponse> adminLogin(
      @RequestBody @Valid final AdminLoginRequest adminLoginRequest,
      @RequestParam final String rsaKeyStrategy) {

    return ResponseEntity.ok(
        authQueryService.adminLogin(adminLoginRequest, RsaKeyStrategy.fromString(rsaKeyStrategy)));
  }

  @DeleteMapping(USER_WITHDRAW)
  public ResponseEntity<Void> withdrawUser(@AuthenticationPrincipal final UUID userId) {

    authService.withdrawUser(userId);
    return ResponseEntity.noContent().build();
  }
}
