package com.ghostchu.btn.btnserver.rule;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuleRepository extends CrudRepository<RuleEntity, Integer> {
    @Override
    Iterable<RuleEntity> findAll();
}
