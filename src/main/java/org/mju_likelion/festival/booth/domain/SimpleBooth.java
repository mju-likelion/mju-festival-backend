package org.mju_likelion.festival.booth.domain;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 부스 간단 정보.
 */
@Getter
@AllArgsConstructor
public class SimpleBooth {

  protected final UUID id;
  protected final String name;
  protected final String description;
  protected final String thumbnailUrl;
}
