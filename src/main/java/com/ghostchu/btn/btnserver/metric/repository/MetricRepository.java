package com.ghostchu.btn.btnserver.metric.repository;

import com.ghostchu.btn.btnserver.metric.entity.MetricEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface MetricRepository extends CrudRepository<MetricEntity, Integer> {
    @Override
    <S extends MetricEntity> S save(S entity);

    @Override
    <S extends MetricEntity> Iterable<S> saveAll(Iterable<S> entities);

    @Override
    Optional<MetricEntity> findById(Integer aLong);

    List<MetricEntity> findByTimeBetween(Timestamp timeStart, Timestamp timeEnd);

    MetricEntity findFirstByOrderByTimeDesc();


}
