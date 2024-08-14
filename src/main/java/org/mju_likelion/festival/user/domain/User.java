package org.mju_likelion.festival.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.booth.domain.BoothUsers;
import org.mju_likelion.festival.common.domain.BaseEntity;
import org.mju_likelion.festival.term.domain.Term;
import org.mju_likelion.festival.term.domain.TermUsers;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "user")
public class User extends BaseEntity {

  @Transient
  private final int STUDENT_ID_LENGTH = 8;

  @Column(name = "student_id", nullable = false, unique = true, length = STUDENT_ID_LENGTH)
  private String studentId;

  @Embedded
  private TermUsers termUsers;

  @Embedded
  private BoothUsers boothUsers;

  public User(final String studentId) {
    this.studentId = studentId;
    this.termUsers = new TermUsers();
    this.boothUsers = new BoothUsers();
  }

  public void agreeToTerm(final Term term) {
    this.termUsers.agree(term, this);
  }

  public void visitBooth(final Booth booth) {
    this.boothUsers.visit(this, booth);
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
