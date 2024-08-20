package org.mju_likelion.festival.lost_item.domain;

import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_LOST_ITEM_TITLE_LENGTH_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.LOST_ITEM_ALREADY_FOUND_ERROR;

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
import org.mju_likelion.festival.common.exception.ConflictException;
import org.mju_likelion.festival.common.exception.type.ErrorType;
import org.mju_likelion.festival.common.util.string.StringUtil;
import org.mju_likelion.festival.image.domain.Image;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true, of = {"title", "content", "retrieverInfo", "image", "writer"})
@Entity(name = "lost_item")
public class LostItem extends BaseEntity {

  @Transient
  private final int TITLE_LENGTH = 70;
  @Transient
  private final int CONTENT_LENGTH = 100;
  @Transient
  private final int OWNER_INFO_LENGTH = 150;

  @Column(nullable = false, length = TITLE_LENGTH)
  private String title;

  @Column(nullable = false, length = CONTENT_LENGTH)
  private String content;

  @Column(length = OWNER_INFO_LENGTH)
  private String retrieverInfo;

  @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(nullable = false, name = "image_id")
  private Image image;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(nullable = false, name = "writer_id")
  private Admin writer;

  public LostItem(
      final String title,
      final String content,
      final Image image,
      final Admin writer) {

    validate(title, content, image, writer);

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
    validateImage(image);
    this.image = image;
  }

  public void found(final String retrieverInfo) {
    if (this.retrieverInfo != null) {
      throw new ConflictException(LOST_ITEM_ALREADY_FOUND_ERROR);
    }
    this.retrieverInfo = retrieverInfo;
  }

  private void validate(
      final String title,
      final String content,
      final Image image,
      final Admin writer) {

    validateTitle(title);
    validateContent(content);
    validateImage(image);
    validateWriter(writer);
  }

  private void validateTitle(final String title) {
    if (StringUtil.isBlankOrLargerThan(title, TITLE_LENGTH)) {
      throw new BadRequestException(INVALID_LOST_ITEM_TITLE_LENGTH_ERROR);
    }
  }

  private void validateContent(final String content) {
    if (StringUtil.isBlankOrLargerThan(content, CONTENT_LENGTH)) {
      throw new BadRequestException(ErrorType.INVALID_LOST_ITEM_CONTENT_LENGTH_ERROR);
    }
  }

  private void validateImage(final Image image) {
    if (image == null) {
      throw new BadRequestException(ErrorType.LOST_ITEM_IMAGE_MISSING_ERROR);
    }
  }

  private void validateWriter(final Admin writer) {
    if (writer == null) {
      throw new BadRequestException(ErrorType.LOST_ITEM_WRITER_MISSING_ERROR);
    }
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof LostItem that)) {
      return false;
    }
    return Objects.equals(title, that.title) &&
        Objects.equals(content, that.content);
  }

  @Override
  public int hashCode() {
    return Objects.hash(title, content);
  }
}
