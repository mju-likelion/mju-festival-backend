package org.mju_likelion.festival.booth.domain;

import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_BOOTH_DESCRIPTION_LENGTH_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_BOOTH_LOCATION_LENGTH_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_BOOTH_NAME_LENGTH_ERROR;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.mju_likelion.festival.common.exception.BadRequestException;
import org.mju_likelion.festival.common.util.string.StringUtil;

@Getter
@Embeddable
@ToString(of = {"name", "description", "location", "affiliation"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoothInfo {

  @Transient
  private final int NAME_LENGTH = 30;
  @Transient
  private final int DESCRIPTION_LENGTH = 1000;
  @Transient
  private final int LOCATION_LENGTH = 100;

  @Column(nullable = false, length = NAME_LENGTH, unique = true)
  private String name;

  @Column(nullable = false, length = DESCRIPTION_LENGTH)
  private String description;

  @Column(nullable = false, length = LOCATION_LENGTH)
  private String location;

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "affiliation_id")
  private BoothAffiliation affiliation;

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "department_id")
  private BoothDepartment department;

  public BoothInfo(
      final String name,
      final String description,
      final String location,
      final BoothAffiliation affiliation,
      final BoothDepartment department) {

    validate(name, description, location);
    this.name = name;
    this.description = description;
    this.location = location;
    this.affiliation = affiliation;
    this.department = department;
  }

  private void validate(final String name, final String description, final String location) {
    validateName(name);
    validateDescription(description);
    validateLocation(location);
  }

  public void validateName(final String name) {
    if (StringUtil.isBlankOrLargerThan(name, NAME_LENGTH)) {
      throw new BadRequestException(INVALID_BOOTH_NAME_LENGTH_ERROR);
    }
  }

  public void validateDescription(final String description) {
    if (StringUtil.isBlankOrLargerThan(description, DESCRIPTION_LENGTH)) {
      throw new BadRequestException(INVALID_BOOTH_DESCRIPTION_LENGTH_ERROR);
    }
  }

  public void validateLocation(final String location) {
    if (StringUtil.isBlankOrLargerThan(location, LOCATION_LENGTH)) {
      throw new BadRequestException(INVALID_BOOTH_LOCATION_LENGTH_ERROR);
    }
  }

  public void updateDescription(final String description) {
    validateDescription(description);
    this.description = description;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof BoothInfo that)) {
      return false;
    }
    return name.equals(that.getName());
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
