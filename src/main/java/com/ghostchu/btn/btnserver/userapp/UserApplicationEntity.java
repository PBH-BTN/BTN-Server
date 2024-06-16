package com.ghostchu.btn.btnserver.userapp;

import com.ghostchu.btn.btnserver.entity.AbstractEntity;
import com.ghostchu.btn.btnserver.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name="userapps", indexes = @Index(columnList = "id, appId, appSecret"))
@Getter
@Setter
public class UserApplicationEntity extends AbstractEntity {
    @Column(nullable = false, updatable = false)
    private String appId;
    @Column(nullable = false)
    private String appSecret;
    @ManyToOne
    private UserEntity user;
    @Column(nullable = false, updatable = false)
    private Timestamp createdAt;
    @Column
    private String comment;
}
