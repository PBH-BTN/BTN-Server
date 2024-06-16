package com.ghostchu.btn.btnserver.userapp;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link UserApplicationEntity}
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserApplicationEntityDto implements Serializable {
    @PositiveOrZero
    Integer id;
    @NotNull
    String appId;
    @NotNull
    String appSecret;
    @NotNull
    Timestamp createdAt;
    String comment;
}