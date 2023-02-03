package com.github.blquinn.standups.domain;

import com.github.blquinn.users.domain.User;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "standup_users")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class StandupUser {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "standup_id")
  @Nonnull
  private Standup standup;
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @Nonnull
  private User user;
}
