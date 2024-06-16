package com.ghostchu.btn.btnserver.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ghostchu.btn.btnserver.entity.AbstractEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name="users", indexes = @Index(columnList = "id, githubIdentifier, nickname"))
@Getter
@Setter
public class UserEntity extends AbstractEntity {
    @Column(nullable = false, unique = true)
    @JsonProperty("github_identifier")
    private String githubIdentifier;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String nickname;
    @Column(nullable = false)
    private String avatar;
    @JsonProperty("register_at")
    @Column(nullable = false, updatable = false)
    private Timestamp registerAt;

}
