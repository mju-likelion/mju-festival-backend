package org.mju_likelion.festival.image.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.mju_likelion.festival.image.domain.Image;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageResponse {

  private final String url;

  public static ImageResponse from(Image image) {
    return new ImageResponse(image.getUrl());
  }
}
