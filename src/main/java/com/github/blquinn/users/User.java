package com.github.blquinn.users;

import com.github.blquinn.standups.StandupUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "username", unique = true, nullable = false)
    @Nonnull
    private String username;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<StandupUser> standupUsers = Set.of();

    public UserDto toDto() {
        return new UserDto(id, username);
    }
}
