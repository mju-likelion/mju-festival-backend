package org.mju_likelion.festival.image.domain;

import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_IMAGE_URL_LENGTH_ERROR;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.mju_likelion.festival.common.domain.BaseEntity;
import org.mju_likelion.festival.common.exception.BadRequestException;
import org.mju_likelion.festival.common.util.string.StringUtil;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, of = {"url"})
@Entity(name = "image")
public class Image extends BaseEntity {

  @Transient
  private final int URL_LENGTH = 256;

  @Column(nullable = false, length = URL_LENGTH)
  private String url;

  public Image(final String url) {
    validateUrl(url);
    this.url = url;
  }

  public void validateUrl(final String url) {
    if (StringUtil.isBlankOrLargerThan(url, URL_LENGTH)) {
      throw new BadRequestException(INVALID_IMAGE_URL_LENGTH_ERROR);
    }
  }
}
