package org.mju_likelion.festival.term.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.mju_likelion.festival.term.domain.repository.TermQueryRepository;
import org.mju_likelion.festival.term.dto.response.TermResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class TermQueryService {

  private final TermQueryRepository termQueryRepository;

  @Transactional(readOnly = true)
  public List<TermResponse> getTerms() {
    return termQueryRepository.findTermsByOrderBySequenceAsc().stream()
        .map(TermResponse::of)
        .collect(Collectors.toList());
  }
}
