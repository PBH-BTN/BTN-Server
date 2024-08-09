package com.ghostchu.btn.btnserver.rule;

import com.ghostchu.btn.btnserver.entity.AbstractEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rules")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RuleEntity extends AbstractEntity {
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private String type;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private long addAt;
    @Column(nullable = false)
    private long expireAt;
}
