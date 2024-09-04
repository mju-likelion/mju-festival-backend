package org.mju_likelion.festival.term.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.mju_likelion.festival.common.exception.BadRequestException;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.mju_likelion.festival.term.domain.Term;
import org.mju_likelion.festival.term.domain.repository.TermJpaRepository;
import org.mju_likelion.festival.term.domain.repository.TermQueryRepository;
import org.mju_likelion.festival.term.dto.response.TermResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TermQueryService {

  private final TermQueryRepository termQueryRepository;
  private final TermJpaRepository termJpaRepository;

  public List<TermResponse> getTerms() {
    return termQueryRepository.findTermsByOrderBySequenceAsc().stream()
        .map(TermResponse::of)
        .collect(Collectors.toList());
  }

  /**
   * 약관 ID 목록을 이용하여 유효한 약관 목록을 반환한다.
   *
   * @param terms 사용자가 동의한 약관
   * @return 유효한 약관 목록
   */
  public List<Term> getValidTerms(final Map<UUID, Boolean> terms) {
    List<Term> termsInDb = termJpaRepository.findAll();
    validateTermIds(termsInDb, terms.keySet());
    return termsInDb;
  }

  /**
   * 약관 ID 목록이 유효한지 검증한다.
   *
   * @param termsInDb DB 의 약관 목록
   * @param termIds   약관 ID 목록
   */
  private void validateTermIds(final List<Term> termsInDb, final Set<UUID> termIds) {
    Set<UUID> validTermIds = termsInDb.stream().map(Term::getId).collect(Collectors.toSet());
    if (!termIds.containsAll(validTermIds)) {
      throw new BadRequestException(ErrorType.MISSING_TERM_ERROR);
    }
  }
}
