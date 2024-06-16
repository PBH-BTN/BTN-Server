package com.ghostchu.btn.btnserver.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class BusinessException extends Exception {
    private final int httpStatusCode;
    private final String message;
}
