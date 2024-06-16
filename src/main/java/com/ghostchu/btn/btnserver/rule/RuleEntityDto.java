package com.ghostchu.btn.btnserver.rule;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO for {@link RuleEntity}
 */
@Data
@NoArgsConstructor
public class RuleEntityDto implements Serializable {
    private Integer id;
    private String category;
    private String type;
    private String content;
}