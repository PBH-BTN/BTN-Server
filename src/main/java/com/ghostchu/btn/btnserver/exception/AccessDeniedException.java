package com.ghostchu.btn.btnserver.exception;

public class AccessDeniedException extends BusinessException {
    public AccessDeniedException() {
        super(401, "Access Denied, You may don't have enough permission to access this module /or you didn't have logged in yet.");
    }
    public AccessDeniedException(String msg) {
        super(401, msg);
    }
}
