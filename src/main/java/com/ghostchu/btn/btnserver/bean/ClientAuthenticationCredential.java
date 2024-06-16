package com.ghostchu.btn.btnserver.bean;

public record ClientAuthenticationCredential(
        String appId,
        String appSecret
) {
    public boolean isValid(){
        return appId != null && appSecret != null;
    }
    public void verifyOrThrow(){
        if(!isValid()){
            throw new IllegalArgumentException("App-ID and App-Secret are both required");
        }
    }
}
