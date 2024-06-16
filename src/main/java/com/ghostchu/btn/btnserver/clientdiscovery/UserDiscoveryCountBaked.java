package com.ghostchu.btn.btnserver.clientdiscovery;

import com.ghostchu.btn.btnserver.user.UserEntityDto;

public class UserDiscoveryCountBaked {
    private UserEntityDto user;
    private Long discoveryCount;

    public UserDiscoveryCountBaked(UserEntityDto user, Long discoveryCount) {
        this.user = user;
        this.discoveryCount = discoveryCount;
    }

    // Getters and setters
    public UserEntityDto getUser() {
        return user;
    }

    public void setUser(UserEntityDto user) {
        this.user = user;
    }

    public Long getDiscoveryCount() {
        return discoveryCount;
    }

    public void setDiscoveryCount(Long discoveryCount) {
        this.discoveryCount = discoveryCount;
    }
}