package org.mju_likelion.festival.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.mju_likelion.festival.common.domain.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, of = {"loginId", "name", "role"})
@Entity(name = "admin")
public class Admin extends BaseEntity {

  @Transient
  private final int NAME_LENGTH = 50;
  @Transient
  private final int ID_LENGTH = 50;
  @Transient
  private final int PASSWORD_LENGTH = 100;

  @Column(nullable = false, unique = true, length = NAME_LENGTH)
  private String loginId;

  @Column(nullable = false, length = PASSWORD_LENGTH)
  private String password;

  @Column(nullable = false, length = NAME_LENGTH)
  private String name;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private AdminRole role;

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Admin that)) {
      return false;
    }
    return Objects.equals(loginId, that.getLoginId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(loginId);
  }
}
