package com.github.blquinn.minimalapi.standups.domain;

import com.github.blquinn.minimalapi.common.jpa.BaseEntity;
import com.github.blquinn.minimalapi.standups.dto.StandupDetailDto;
import com.github.blquinn.minimalapi.standups.dto.StandupDto;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Standup extends BaseEntity {
  @Nonnull
  @Column(nullable = false)
  private String name;
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "standup")
  private Set<StandupUser> standupUsers = Set.of();

  public StandupDto toDto() {
    return new StandupDto(id, name);
  }

  public StandupDetailDto toDetailDto() {
    return new StandupDetailDto(id, name,
        standupUsers.stream().map(su -> su.getUser().toDto()).toList());
  }
}
