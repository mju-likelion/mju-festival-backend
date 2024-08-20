package org.mju_likelion.festival.booth.domain;

import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_BOOTH_DESCRIPTION_LENGTH_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_BOOTH_LOCATION_LENGTH_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_BOOTH_NAME_LENGTH_ERROR;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.common.domain.BaseEntity;
import org.mju_likelion.festival.common.exception.BadRequestException;
import org.mju_likelion.festival.common.util.string.StringUtil;
import org.mju_likelion.festival.image.domain.Image;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true,
    of = {"name", "description", "location", "sequence", "locationImage", "image"})
@Entity(name = "booth")
public class Booth extends BaseEntity {

  @Transient
  private final int NAME_LENGTH = 100;
  @Transient
  private final int DESCRIPTION_LENGTH = 4000;
  @Transient
  private final int LOCATION_LENGTH = 100;

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id")
  private Admin owner;

  @Column(nullable = false, length = NAME_LENGTH, unique = true)
  private String name;

  @Column(nullable = false, length = DESCRIPTION_LENGTH)
  private String description;

  @Column(nullable = false, length = LOCATION_LENGTH)
  private String location;

  @Column(nullable = false)
  private Short sequence;

  @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(nullable = false, name = "location_image_id")
  private Image locationImage;

  @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(nullable = false, name = "image_id")
  private Image image;

  public Booth(
      final Admin owner,
      final String name,
      final String description,
      final String location,
      final Short sequence,
      final Image locationImage,
      final Image image) {

    validate(name, description, location);
    this.owner = owner;
    this.name = name;
    this.description = description;
    this.location = location;
    this.sequence = sequence;
    this.locationImage = locationImage;
    this.image = image;
  }

  public boolean isManagedBy(final Admin admin) {
    return owner.equals(admin);
  }

  public void updateName(final String name) {
    validateName(name);
    this.name = name;
  }

  public void updateDescription(final String description) {
    validateDescription(description);
    this.description = description;
  }

  public void updateLocation(final String location) {
    validateLocation(location);
    this.location = location;
  }

  public void updateLocationImage(final Image locationImage) {
    this.locationImage = locationImage;
  }

  public void updateImage(final Image image) {
    this.image = image;
  }

  public void validate(
      final String name,
      final String description,
      final String location) {

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

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Booth that)) {
      return false;
    }
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
