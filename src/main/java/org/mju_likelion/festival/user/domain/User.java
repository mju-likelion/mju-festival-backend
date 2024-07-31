package org.mju_likelion.festival.user.domain;

import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.USER_STUDENT_ID_LENGTH;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.booth.domain.BoothUser;
import org.mju_likelion.festival.common.domain.BaseEntity;
import org.mju_likelion.festival.term.domain.Term;
import org.mju_likelion.festival.term.domain.TermUsers;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "user")
public class User extends BaseEntity {

  @Column(name = "student_id", nullable = false, unique = true, length = USER_STUDENT_ID_LENGTH)
  private String studentId;

  @Embedded
  private TermUsers termUsers;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<BoothUser> boothUsers;

  public User(String studentId) {
    this.studentId = studentId;
    this.termUsers = new TermUsers();
    this.boothUsers = new ArrayList<>();
  }

  public void agreeToTerm(Term term) {
    this.termUsers.agree(term, this);
  }

  @Override
  public String toString() {
    return "User{" +
        "studentId='" + studentId + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof User user)) {
      return false;
    }
    return this.id.equals(user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.id);
  }
}
