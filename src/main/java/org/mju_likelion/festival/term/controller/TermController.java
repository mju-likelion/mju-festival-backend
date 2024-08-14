package org.mju_likelion.festival.term.controller;

import java.util.List;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.term.dto.response.TermResponse;
import org.mju_likelion.festival.term.service.TermQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class TermController {

  private final TermQueryService termQueryService;

  @GetMapping("/terms")
  public ResponseEntity<List<TermResponse>> getTerms() {
    return ResponseEntity.ok(termQueryService.getTerms());
  }
}
