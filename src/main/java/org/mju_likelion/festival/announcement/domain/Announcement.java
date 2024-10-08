package org.mju_likelion.festival.announcement.domain;

import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_ANNOUNCEMENT_CONTENT_LENGTH_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_ANNOUNCEMENT_TITLE_LENGTH_ERROR;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Transient;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.common.domain.BaseEntity;
import org.mju_likelion.festival.common.exception.BadRequestException;
import org.mju_likelion.festival.common.util.string.StringUtil;
import org.mju_likelion.festival.image.domain.Image;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, of = {"title", "content", "image", "writer"})
@Entity(name = "announcement")
public class Announcement extends BaseEntity {

  @Transient
  private final int TITLE_LENGTH = 30;
  @Transient
  private final int CONTENT_LENGTH = 1000;

  @Column(nullable = false, length = TITLE_LENGTH)
  private String title;

  @Column(nullable = false, length = CONTENT_LENGTH)
  private String content;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "image_id")
  private Image image;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "writer_id")
  private Admin writer;

  public Announcement(
      final String title,
      final String content,
      final Image image,
      final Admin writer) {

    validate(title, content);
    this.title = title;
    this.content = content;
    this.image = image;
    this.writer = writer;
  }

  public void updateTitle(final String title) {
    validateTitle(title);
    this.title = title;
  }

  public void updateContent(final String content) {
    validateContent(content);
    this.content = content;
  }

  public void updateImage(final Image image) {
    this.image = image;
  }

  private void validate(final String title, final String content) {
    validateTitle(title);
    validateContent(content);
  }

  private void validateTitle(final String title) {
    if (StringUtil.isBlankOrLargerThan(title, TITLE_LENGTH)) {
      throw new BadRequestException(INVALID_ANNOUNCEMENT_TITLE_LENGTH_ERROR);
    }
  }

  private void validateContent(final String content) {
    if (StringUtil.isBlankOrLargerThan(content, CONTENT_LENGTH)) {
      throw new BadRequestException(INVALID_ANNOUNCEMENT_CONTENT_LENGTH_ERROR);
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Announcement that)) {
      return false;
    }
    return Objects.equals(title, that.getTitle()) &&
        Objects.equals(content, that.getContent());
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, content);
  }
}
