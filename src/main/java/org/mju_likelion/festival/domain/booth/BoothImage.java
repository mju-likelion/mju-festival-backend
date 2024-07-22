package org.mju_likelion.festival.domain.booth;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.domain.common.BaseEntity;
import org.mju_likelion.festival.domain.image.Image;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "booth_image")
public class BoothImage extends BaseEntity {

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "booth_id", nullable = false)
  private Booth booth;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "image_id", nullable = false)
  private Image image;
}
