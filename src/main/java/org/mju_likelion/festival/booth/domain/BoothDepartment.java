package org.mju_likelion.festival.booth.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.mju_likelion.festival.common.domain.BaseEntity;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, of = {"name"})
@Entity(name = "booth_department")
public class BoothDepartment extends BaseEntity {

  @Transient
  private final int NAME_LENGTH = 20;

  @Column(nullable = false, length = NAME_LENGTH, unique = true)
  private String name;

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final BoothDepartment that = (BoothDepartment) o;
    return name.equals(that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
