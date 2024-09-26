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
@ToString(callSuper = true, of = {"name", "categoryName"})
@Entity(name = "booth_affiliation")
public class BoothAffiliation extends BaseEntity {

  @Transient
  private final int NAME_LENGTH = 15;
  @Transient
  private final int CATEGORY_NAME_LENGTH = 15;

  @Column(nullable = false, length = NAME_LENGTH, unique = true)
  private String name;

  @Column(nullable = false, length = CATEGORY_NAME_LENGTH, unique = true)
  private String categoryName;

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    final BoothAffiliation that = (BoothAffiliation) o;
    return Objects.equals(name, that.name) && Objects.equals(categoryName, that.categoryName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name) + Objects.hash(categoryName);
  }
}
