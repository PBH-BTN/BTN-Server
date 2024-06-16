package com.ghostchu.btn.btnserver.metric.entity;

import com.ghostchu.btn.btnserver.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "metrics", indexes = @Index(columnList = "id, time"))
@Getter
@Setter
public class MetricEntity extends AbstractEntity {
    @Column(nullable = false)
    private Timestamp time;
    @Column(nullable = false)
    private Long onlineClients;
    @Column(nullable = false)
    private Long onlinePeers;
    @Column(nullable = false)
    private Long bannedPeers;
    @Column(nullable = false)
    private Long bannedBtnPeers;
    @Column(nullable = false)
    private Long registeredUsers;
    @Column(nullable = false)
    private Long registeredClients;
}

