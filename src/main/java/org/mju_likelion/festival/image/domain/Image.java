package org.mju_likelion.festival.image.domain;

import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.IMAGE_URL_LENGTH;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.announcement.domain.AnnouncementImage;
import org.mju_likelion.festival.booth.domain.Booth;
import org.mju_likelion.festival.booth.domain.BoothImage;
import org.mju_likelion.festival.common.domain.BaseEntity;
import org.mju_likelion.festival.lost_item.domain.LostItemImage;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "image")
public class Image extends BaseEntity {

  @Column(nullable = false, length = IMAGE_URL_LENGTH, unique = true)
  private String url;

  @OneToOne(mappedBy = "thumbnail", fetch = FetchType.LAZY)
  private Booth booth;

  @OneToMany(mappedBy = "image", fetch = FetchType.LAZY)
  private List<LostItemImage> lostItemImages;

  @OneToMany(mappedBy = "image", fetch = FetchType.LAZY)
  private List<BoothImage> boothImages;

  @OneToMany(mappedBy = "image", fetch = FetchType.LAZY)
  private List<AnnouncementImage> announcementImages;
}
