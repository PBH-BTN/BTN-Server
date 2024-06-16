package com.ghostchu.btn.btnserver.clientdiscovery;

import com.ghostchu.btn.btnserver.user.UserEntityDto;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link ClientDiscoveryEntity}
 */
@Data
public class ClientDiscoveryEntityDto implements Serializable {
    private String id;
    private String peerId;
    private String clientName;
    private UserEntityDto foundBy;
    private String foundAtAddress;
    private Timestamp foundAt;
}