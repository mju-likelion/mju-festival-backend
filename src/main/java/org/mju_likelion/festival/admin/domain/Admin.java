package org.mju_likelion.festival.admin.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.announcement.domain.Announcement;
import org.mju_likelion.festival.common.domain.BaseEntity;
import org.mju_likelion.festival.lost_item.domain.LostItem;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "admin")
public class Admin extends BaseEntity {

  private final int NAME_LENGTH = 50;
  private final int ID_LENGTH = 50;
  private final int PASSWORD_LENGTH = 100;
  private final int ROLE_LENGTH = 20;

  @Column(nullable = false, unique = true, length = NAME_LENGTH)
  private String loginId;

  @Column(nullable = false, length = PASSWORD_LENGTH)
  private String password;

  @Column(nullable = false, length = NAME_LENGTH)
  private String name;

  @Column(nullable = false, length = ROLE_LENGTH)
  @Enumerated(EnumType.STRING)
  private AdminRole role;

  @OneToMany(mappedBy = "writer", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private List<LostItem> lostItems;

  @OneToMany(mappedBy = "writer", cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
  private List<Announcement> announcements;
}
