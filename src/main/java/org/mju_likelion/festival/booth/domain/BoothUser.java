package org.mju_likelion.festival.booth.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mju_likelion.festival.common.domain.BaseEntity;
import org.mju_likelion.festival.user.domain.User;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "booth_user")
public class BoothUser extends BaseEntity {

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "booth_id", nullable = false)
  private Booth booth;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
