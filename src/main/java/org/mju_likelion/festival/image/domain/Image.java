package org.mju_likelion.festival.image.domain;

import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.IMAGE_URL_LENGTH;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.announcement.domain.AnnouncementImage;
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

  @OneToMany(mappedBy = "image", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<LostItemImage> lostItemImages;

  @OneToMany(mappedBy = "image", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<BoothImage> boothImages;

  @OneToMany(mappedBy = "image", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<AnnouncementImage> announcementImages;
}
