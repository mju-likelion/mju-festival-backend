package org.mju_likelion.festival.booth.domain;

import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.BOOTH_DESCRIPTION_LENGTH;
import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.BOOTH_LOCATION_LENGTH;
import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.BOOTH_NAME_LENGTH;
import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_BOOTH_DESCRIPTION_LENGTH_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_BOOTH_LOCATION_LENGTH_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_BOOTH_NAME_LENGTH_ERROR;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.common.domain.BaseEntity;
import org.mju_likelion.festival.common.exception.BadRequestException;
import org.mju_likelion.festival.common.util.string.StringUtil;
import org.mju_likelion.festival.image.domain.Image;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "booth")
public class Booth extends BaseEntity {

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id")
  private Admin owner;

  @Column(nullable = false, length = BOOTH_NAME_LENGTH, unique = true)
  private String name;

  @Column(nullable = false, length = BOOTH_DESCRIPTION_LENGTH)
  private String description;

  @Column(nullable = false, length = BOOTH_LOCATION_LENGTH)
  private String location;

  @Column(nullable = false)
  private Short sequence;

  @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(nullable = false, name = "location_image_id")
  private Image locationImage;

  @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(nullable = false, name = "image_id")
  private Image image;

  public Booth(Admin owner, String name, String description, String location, Short sequence,
      Image locationImage, Image image) {

    validate(name, description, location);
    this.owner = owner;
    this.name = name;
    this.description = description;
    this.location = location;
    this.sequence = sequence;
    this.locationImage = locationImage;
    this.image = image;
  }

  public boolean isManagedBy(Admin admin) {
    return owner.equals(admin);
  }

  public void updateName(String name) {
    validateName(name);
    this.name = name;
  }

  public void updateDescription(String description) {
    validateDescription(description);
    this.description = description;
  }

  public void updateLocation(String location) {
    validateLocation(location);
    this.location = location;
  }

  public void updateLocationImage(Image locationImage) {
    this.locationImage = locationImage;
  }

  public void updateImage(Image image) {
    this.image = image;
  }

  public void validate(String name, String description, String location) {
    validateName(name);
    validateDescription(description);
    validateLocation(location);
  }

  public void validateName(String name) {
    if (StringUtil.isBlankOrLargerThan(name, BOOTH_NAME_LENGTH)) {
      throw new BadRequestException(INVALID_BOOTH_NAME_LENGTH_ERROR);
    }
  }

  public void validateDescription(String description) {
    if (StringUtil.isBlankOrLargerThan(description, BOOTH_DESCRIPTION_LENGTH)) {
      throw new BadRequestException(INVALID_BOOTH_DESCRIPTION_LENGTH_ERROR);
    }
  }

  public void validateLocation(String location) {
    if (StringUtil.isBlankOrLargerThan(location, BOOTH_LOCATION_LENGTH)) {
      throw new BadRequestException(INVALID_BOOTH_LOCATION_LENGTH_ERROR);
    }
  }
}
