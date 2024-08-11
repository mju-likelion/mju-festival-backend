package org.mju_likelion.festival.announcement.domain;

import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.ANNOUNCEMENT_CONTENT_LENGTH;
import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.ANNOUNCEMENT_TITLE_LENGTH;
import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_CONTENT_LENGTH_ERROR;
import static org.mju_likelion.festival.common.exception.type.ErrorType.INVALID_TITLE_LENGTH_ERROR;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.common.domain.BaseEntity;
import org.mju_likelion.festival.common.exception.BadRequestException;
import org.mju_likelion.festival.common.util.string.StringUtil;
import org.mju_likelion.festival.image.domain.Image;

@Getter
@NoArgsConstructor
@Entity(name = "announcement")
public class Announcement extends BaseEntity {

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "writer_id")
  private Admin writer;

  @Column(nullable = false, length = ANNOUNCEMENT_TITLE_LENGTH)
  private String title;

  @Column(nullable = false, length = ANNOUNCEMENT_CONTENT_LENGTH)
  private String content;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "image_id")
  private Image image;

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

  private void validate(final String title, final String content) {
    validateTitle(title);
    validateContent(content);
  }

  private void validateTitle(final String title) {
    if (StringUtil.isEmptyOrLargerThan(title, ANNOUNCEMENT_TITLE_LENGTH)) {
      throw new BadRequestException(INVALID_TITLE_LENGTH_ERROR);
    }
  }

  private void validateContent(final String content) {
    if (StringUtil.isEmptyOrLargerThan(content, ANNOUNCEMENT_CONTENT_LENGTH)) {
      throw new BadRequestException(INVALID_CONTENT_LENGTH_ERROR);
    }
  }
}
