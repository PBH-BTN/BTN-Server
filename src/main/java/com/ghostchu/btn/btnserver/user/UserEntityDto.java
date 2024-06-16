package com.ghostchu.btn.btnserver.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * DTO for {@link UserEntity}
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserEntityDto implements Serializable {
    @PositiveOrZero
    Integer id;
    @NotNull
    String githubIdentifier;
    @NotNull
    String nickname;
    @NotNull
    String avatar;
    Timestamp registerAt;
}