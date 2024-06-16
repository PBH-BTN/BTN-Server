package com.ghostchu.btn.btnserver.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import cn.dev33.satoken.exception.SaTokenException;
import com.ghostchu.btn.btnserver.bean.MsgBean;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.IOException;

@ControllerAdvice
@Slf4j
public class ExceptionControllerHandler {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<MsgBean> exceptionHandler(Exception e) {
        log.error("Unexpected exception", e);
        return ResponseEntity.internalServerError().body(new MsgBean("Server Internal Error: " + e.getClass().getName() + ": " + e.getMessage()));
    }

    @ExceptionHandler(value = BusinessException.class)
    public ResponseEntity<MsgBean> exceptionHandler(BusinessException e) {
        log.warn("Business error (Business): {}",e.getMessage());
        return ResponseEntity.status(e.getHttpStatusCode()).body(new MsgBean(e.getMessage()));
    }

    @ExceptionHandler(value = SaTokenException.class)
    public ResponseEntity<MsgBean> exceptionHandler(SaTokenException e) {
        log.warn("Access Denied (General): {}",e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MsgBean(e.getMessage()));
    }

    @ExceptionHandler(value = NotLoginException.class)
    public ResponseEntity<MsgBean> exceptionHandler(NotLoginException e) throws IOException {
        log.warn("Access Denied (Not Login): {}",e.getMessage());
        String accept = request.getHeader("Accept");
        if(accept != null && accept.contains("text/html")) {
            response.sendRedirect("/auth/oauth2/github/login");
            return ResponseEntity.status(302).header("Location","/auth/oauth2/github/login").body(new MsgBean(e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MsgBean(e.getMessage()));
    }

    @ExceptionHandler(value = NotPermissionException.class)
    public ResponseEntity<MsgBean> exceptionHandler(NotPermissionException e) {
        log.warn("Access Denied (Not Permission): {}",e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MsgBean(e.getMessage()));
    }

    @ExceptionHandler(value = NotRoleException.class)
    public ResponseEntity<MsgBean> exceptionHandler(NotRoleException e) {
        log.warn("Access Denied (Not Role): {}",e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new MsgBean(e.getMessage()));
    }
    @ExceptionHandler(value = NoResourceFoundException.class)
    public ResponseEntity<MsgBean> exceptionHandler(NoResourceFoundException e) {
        log.warn("404 Not Found: {}",e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MsgBean(e.getMessage()));
    }
}
