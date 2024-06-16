package com.ghostchu.btn.btnserver.exception;

public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException() {
        super(404, "Resource not found");
    }
    public ResourceNotFoundException(String msg) {
        super(404, msg);
    }
}
