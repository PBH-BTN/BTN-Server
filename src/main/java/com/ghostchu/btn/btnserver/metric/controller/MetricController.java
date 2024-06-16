package com.ghostchu.btn.btnserver.metric.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.ghostchu.btn.btnserver.metric.MetricService;
import com.ghostchu.btn.btnserver.metric.entity.MetricEntity;
import com.ghostchu.btn.btnserver.metric.entity.MetricEntityDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/metric")
public class MetricController {
    @Autowired
    private MetricService metricService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/view")
    @SaCheckLogin
    public List<MetricEntityDto> view(@RequestParam(value = "from", required = false) Long from, @RequestParam(value = "to", required = false) Long to) {
        if (from == null) {
            from = Instant.now().minus(14, ChronoUnit.DAYS).toEpochMilli();
        }
        if (to == null) {
            to = Instant.now().toEpochMilli();
        }
        List<MetricEntity> entities = metricService.fetchMetricsBetween(new Timestamp(from), new Timestamp(to));
        return entities.stream().map(e -> modelMapper.map(e, MetricEntityDto.class)).toList();
    }

    @GetMapping("/last")
    @SaCheckLogin
    public MetricEntityDto last() {
        return modelMapper.map(metricService.fetchLastMetricRecord(), MetricEntityDto.class);
    }
}
