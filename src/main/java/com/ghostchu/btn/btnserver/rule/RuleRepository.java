package com.ghostchu.btn.btnserver.rule;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RuleRepository extends CrudRepository<RuleEntity, Integer> {
    @Override
    Iterable<RuleEntity> findAll();

    @Query("select r from RuleEntity r where r.expireAt <= ?1")
    List<RuleEntity> findByExpireAtLessThanEqual(long expireAt);


}
