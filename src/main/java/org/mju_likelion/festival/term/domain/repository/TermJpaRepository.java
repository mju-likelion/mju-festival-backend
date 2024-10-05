package org.mju_likelion.festival.term.domain.repository;

import java.util.UUID;
import org.mju_likelion.festival.term.domain.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TermJpaRepository extends JpaRepository<Term, UUID> {

}
