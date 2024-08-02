package org.mju_likelion.festival.image.domain;

import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.IMAGE_URL_LENGTH;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.announcement.domain.AnnouncementImage;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.booth.domain.BoothImage;
import org.mju_likelion.festival.common.domain.BaseEntity;
import org.mju_likelion.festival.lost_item.domain.LostItemImage;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "image")
public class Image extends BaseEntity {

  @OneToMany(mappedBy = "image", fetch = FetchType.LAZY)
  private final List<LostItemImage> lostItemImages = new ArrayList<>();
  @OneToMany(mappedBy = "image", fetch = FetchType.LAZY)
  private final List<BoothImage> boothImages = new ArrayList<>();
  @OneToMany(mappedBy = "image", fetch = FetchType.LAZY)
  private final List<AnnouncementImage> announcementImages = new ArrayList<>();
  @Column(nullable = false, length = IMAGE_URL_LENGTH, unique = true)
  private String url;
  @OneToOne(mappedBy = "thumbnail", fetch = FetchType.LAZY)
  private Booth booth;

  public Image(String url) {
    this.url = url;
  }
}
