package com.github.blquinn.minimalapi.standups.domain;

import com.github.blquinn.minimalapi.common.jpa.BaseEntity;
import com.github.blquinn.minimalapi.users.domain.User;
import javax.annotation.Nonnull;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class StandupUser extends BaseEntity {
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  @Nonnull
  private Standup standup;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(nullable = false)
  @Nonnull
  private User user;
}
