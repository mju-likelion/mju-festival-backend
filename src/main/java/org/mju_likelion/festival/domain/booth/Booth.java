package org.mju_likelion.festival.domain.booth;

import static org.mju_likelion.festival.domain.common.constant.ColumnLengths.BOOTH_DESCRIPTION_LENGTH;
import static org.mju_likelion.festival.domain.common.constant.ColumnLengths.BOOTH_NAME_LENGTH;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
@Entity(name = "booth")
public class Booth extends BaseEntity {

  @OneToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "owner_id")
  private Admin owner;

  @Column(nullable = false, length = BOOTH_NAME_LENGTH, unique = true)
  private String name;

  @Column(nullable = false, length = BOOTH_DESCRIPTION_LENGTH)
  private String description;

  @OneToMany(mappedBy = "booth", fetch = FetchType.LAZY)
  private List<BoothUser> boothUsers;

  @OneToMany(mappedBy = "booth", fetch = FetchType.LAZY)
  private List<BoothImage> boothImages;
}
