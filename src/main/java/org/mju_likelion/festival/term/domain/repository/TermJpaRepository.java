package org.mju_likelion.festival.term.domain.repository;

import java.util.List;
import java.util.UUID;
import org.mju_likelion.festival.term.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermJpaRepository extends JpaRepository<Term, UUID> {

  /**
   * Term 의 Order 를 기준으로 오름차순으로 정렬하여 모든 Term 조회.
   *
   * @return Term 의 Order 를 기준으로 오름차순으로 정렬된 모든 Term
   */
  List<Term> findTermsByOrderByOrderAsc();
}
