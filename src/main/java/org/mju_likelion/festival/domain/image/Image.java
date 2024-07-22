package org.mju_likelion.festival.domain.image;

import static org.mju_likelion.festival.domain.common.constant.ColumnLengths.IMAGE_URL_LENGTH;

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
import org.mju_likelion.festival.domain.announcement.AnnouncementImage;
import org.mju_likelion.festival.domain.booth.BoothImage;
import org.mju_likelion.festival.domain.common.BaseEntity;
import org.mju_likelion.festival.domain.lost_item.LostItemImage;

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
