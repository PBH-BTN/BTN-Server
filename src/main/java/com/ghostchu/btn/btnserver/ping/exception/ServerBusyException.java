package com.ghostchu.btn.btnserver.ping.exception;

import com.ghostchu.btn.btnserver.exception.BusinessException;

public class ServerBusyException extends BusinessException {
    public ServerBusyException(String msg) {
        super(503, msg);
    }
}
