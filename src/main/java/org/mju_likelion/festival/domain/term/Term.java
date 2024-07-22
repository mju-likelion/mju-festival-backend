package org.mju_likelion.festival.domain.term;

import static org.mju_likelion.festival.domain.common.constant.ColumnLengths.TERM_CONTENT_LENGTH;
import static org.mju_likelion.festival.domain.common.constant.ColumnLengths.TERM_TITLE_LENGTH;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.Getter;
import org.mju_likelion.festival.domain.common.BaseEntity;

@Getter
@Entity(name = "term")
public class Term extends BaseEntity {

  @Column(nullable = false, length = TERM_TITLE_LENGTH)
  private String title;

  @Column(nullable = false, length = TERM_CONTENT_LENGTH)
  private String content;

  @OneToMany(mappedBy = "term", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private List<TermUser> termUsers;
}
