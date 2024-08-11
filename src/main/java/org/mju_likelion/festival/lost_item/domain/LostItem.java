package org.mju_likelion.festival.lost_item.domain;

import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.LOST_ITEM_CONTENT_LENGTH;
import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.LOST_ITEM_OWNER_INFO_LENGTH;
import static org.mju_likelion.festival.common.domain.constant.ColumnLengths.LOST_ITEM_TITLE_LENGTH;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.admin.domain.Admin;
import org.mju_likelion.festival.common.domain.BaseEntity;
import org.mju_likelion.festival.image.domain.Image;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "lost_item")
public class LostItem extends BaseEntity {

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "writer_id", nullable = false)
  private Admin writer;

  @Column(nullable = false, length = LOST_ITEM_TITLE_LENGTH)
  private String title;

  @Column(nullable = false, length = LOST_ITEM_CONTENT_LENGTH)
  private String content;

  @Column(nullable = false, length = LOST_ITEM_OWNER_INFO_LENGTH)
  private String retrieverInfo;

  @OneToOne(optional = false, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(nullable = false, name = "image_id")
  private Image image;
}
