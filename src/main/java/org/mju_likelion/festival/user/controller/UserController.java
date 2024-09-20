package org.mju_likelion.festival.user.controller;

import static org.mju_likelion.festival.common.api.ApiPaths.GET_MY_STAMP;
import static org.mju_likelion.festival.common.api.ApiPaths.USER_WITHDRAW;

import java.util.UUID;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.common.authentication.AuthenticationPrincipal;
import org.mju_likelion.festival.user.dto.response.StampResponse;
import org.mju_likelion.festival.user.service.UserQueryService;
import org.mju_likelion.festival.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {

  private final UserQueryService userQueryService;
  private final UserService userService;

  @GetMapping(GET_MY_STAMP)
  public ResponseEntity<StampResponse> getMyStamp(@AuthenticationPrincipal final UUID userId) {
    return ResponseEntity.ok(userQueryService.getUserStampById(userId));
  }

  @DeleteMapping(USER_WITHDRAW)
  public ResponseEntity<Void> withdrawUser(@AuthenticationPrincipal final UUID userId) {

    userService.withdrawUser(userId);
    return ResponseEntity.noContent().build();
  }
}
