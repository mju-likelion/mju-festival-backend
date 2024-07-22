package org.mju_likelion.festival.user.domain;

import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.USER_STUDENT_ID_LENGTH;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.booth.domain.BoothUser;
import org.mju_likelion.festival.common.domain.BaseEntity;
import org.mju_likelion.festival.term.domain.TermUser;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user")
public class User extends BaseEntity {

  @Column(name = "student_id", nullable = false, unique = true, length = USER_STUDENT_ID_LENGTH)
  private String studentId;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<TermUser> termUsers;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<BoothUser> boothUsers;
}
