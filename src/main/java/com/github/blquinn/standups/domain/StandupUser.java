package com.github.blquinn.standups.domain;

import com.github.blquinn.users.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.annotation.Nonnull;
import javax.persistence.*;

@Entity
@Table(name = "standup_users")
@Getter @Setter @NoArgsConstructor @RequiredArgsConstructor
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
