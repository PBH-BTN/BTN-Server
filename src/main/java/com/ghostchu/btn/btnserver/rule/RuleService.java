package com.ghostchu.btn.btnserver.rule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RuleService {
    @Autowired
    private RuleRepository repository;

    public List<RuleEntity> readRules() {
        List<RuleEntity> ruleEntities = new ArrayList<>();
        repository.findAll().forEach(ruleEntities::add);
        return ruleEntities;
    }
}
