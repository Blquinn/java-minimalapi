package com.github.blquinn.users.domain;

import com.github.blquinn.common.jpa.BaseEntity;
import com.github.blquinn.standups.domain.StandupUser;
import com.github.blquinn.users.dto.UserDto;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class User extends BaseEntity {
  @Column(unique = true, nullable = false)
  @Nonnull
  private String username;
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
  private Set<StandupUser> standupUsers = Set.of();

  public UserDto toDto() {
    return new UserDto(id, username);
  }
}
