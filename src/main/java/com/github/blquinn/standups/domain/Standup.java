package com.github.blquinn.standups.domain;

import com.github.blquinn.standups.dto.StandupDetailDto;
import com.github.blquinn.standups.dto.StandupDto;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "standup")
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
public class Standup {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;

  @Column(name = "name")
  @Nonnull
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
