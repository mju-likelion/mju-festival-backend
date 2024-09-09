package org.mju_likelion.festival.booth.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.common.domain.BaseEntity;
import org.mju_likelion.festival.image.domain.Image;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true,
    of = {"boothInfo", "locationImage", "image"})
@Entity(name = "booth")
public class Booth extends BaseEntity {

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id")
  private Admin owner;

  @Embedded
  private BoothInfo boothInfo;

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
      final BoothInfo boothInfo,
      final Short sequence,
      final Image locationImage,
      final Image image) {

    this.owner = owner;
    this.boothInfo = boothInfo;
    this.sequence = sequence;
    this.locationImage = locationImage;
    this.image = image;
  }

  public boolean isManagedBy(final Admin admin) {
    return owner.equals(admin);
  }

  public void updateLocationImage(final Image locationImage) {
    this.locationImage = locationImage;
  }

  public void updateImage(final Image image) {
    this.image = image;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Booth that)) {
      return false;
    }
    return Objects.equals(boothInfo, that.getBoothInfo());
  }

  @Override
  public int hashCode() {
    return Objects.hash(boothInfo.hashCode());
  }
}
