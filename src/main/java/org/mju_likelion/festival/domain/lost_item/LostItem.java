package org.mju_likelion.festival.domain.lost_item;

import static org.mju_likelion.festival.domain.common.constant.ColumnLengths.LOST_ITEM_CONTENT_LENGTH;
import static org.mju_likelion.festival.domain.common.constant.ColumnLengths.LOST_ITEM_OWNER_INFO_LENGTH;
import static org.mju_likelion.festival.domain.common.constant.ColumnLengths.LOST_ITEM_TITLE_LENGTH;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.domain.admin.Admin;
import org.mju_likelion.festival.domain.common.BaseEntity;

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

  @OneToMany(mappedBy = "lostItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<LostItemImage> lostItemImages;
}
