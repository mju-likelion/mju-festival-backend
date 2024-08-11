package org.mju_likelion.festival.booth.domain;

import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.BOOTH_DESCRIPTION_LENGTH;
import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.BOOTH_LOCATION_LENGTH;
import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.BOOTH_NAME_LENGTH;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.common.domain.BaseEntity;
import org.mju_likelion.festival.image.domain.Image;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

  public boolean isManagedBy(Admin admin) {
    return owner.equals(admin);
  }
}
