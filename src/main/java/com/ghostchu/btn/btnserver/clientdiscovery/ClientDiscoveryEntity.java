package com.ghostchu.btn.btnserver.clientdiscovery;

import com.ghostchu.btn.btnserver.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name="client_discovery",uniqueConstraints = { @UniqueConstraint(columnNames = { "peerId", "clientName" }) },
        indexes = @Index(columnList = "id, foundAt"))
@Getter
@Setter
public class ClientDiscoveryEntity  {
    @Id
    @Column(nullable = false, updatable = false)
    private String id;
    @Column
    private String peerId;
    @Column
    private String clientName;
    @ManyToOne
    private UserEntity foundBy;
    @Column
    private String foundAtAddress;
    @Column
    private Timestamp foundAt;
}
