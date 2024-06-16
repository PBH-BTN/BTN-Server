package com.ghostchu.btn.btnserver.clientdiscovery;

public class UserDiscoveryCount {
    private Integer userId;
    private Long discoveryCount;

    public UserDiscoveryCount(Integer userId, Long discoveryCount) {
        this.userId = userId;
        this.discoveryCount = discoveryCount;
    }

    // Getters and setters
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getDiscoveryCount() {
        return discoveryCount;
    }

    public void setDiscoveryCount(Long discoveryCount) {
        this.discoveryCount = discoveryCount;
    }
}