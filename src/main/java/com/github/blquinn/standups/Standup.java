package com.github.blquinn.standups;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "standup")
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor
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
