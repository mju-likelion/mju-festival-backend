package org.mju_likelion.festival.image.util.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 이미지 타입 Enum.
 */
@Getter
@AllArgsConstructor
public enum ImageType {
  BOOTH("booth/"),
  LOST_ITEM("lost_item/"),
  ANNOUNCEMENT("announcement/");

  private final String location;
}
