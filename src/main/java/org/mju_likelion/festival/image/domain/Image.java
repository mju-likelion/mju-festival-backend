package org.mju_likelion.festival.image.domain;

import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.IMAGE_URL_LENGTH;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.announcement.domain.Announcement;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.common.domain.BaseEntity;
import org.mju_likelion.festival.lost_item.domain.LostItem;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "image")
public class Image extends BaseEntity {

  @OneToOne(mappedBy = "image", fetch = FetchType.LAZY)
  private Booth boothThumbnail;

  @OneToOne(mappedBy = "image", fetch = FetchType.LAZY)
  private Booth boothImage;

  @OneToOne(mappedBy = "image", fetch = FetchType.LAZY)
  private LostItem lostItem;

  @OneToOne(mappedBy = "image", fetch = FetchType.LAZY)
  private Announcement announcement;

  @Column(nullable = false, length = IMAGE_URL_LENGTH, unique = true)
  private String url;

  public Image(String url) {
    this.url = url;
  }
}
