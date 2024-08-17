package org.mju_likelion.festival.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.common.domain.BaseEntity;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity(name = "admin")
public class Admin extends BaseEntity {

  @Transient
  private final int NAME_LENGTH = 50;
  @Transient
  private final int ID_LENGTH = 50;
  @Transient
  private final int PASSWORD_LENGTH = 100;

  @Column(nullable = false, unique = true, length = NAME_LENGTH)
  private String loginId;

  @Column(nullable = false, length = PASSWORD_LENGTH)
  private String password;

  @Column(nullable = false, length = NAME_LENGTH)
  private String name;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private AdminRole role;
}
