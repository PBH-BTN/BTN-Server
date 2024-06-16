package com.ghostchu.btn.btnserver.metric;

import com.ghostchu.btn.btnserver.metric.entity.MetricEntity;
import com.ghostchu.btn.btnserver.metric.repository.MetricRepository;
import com.ghostchu.btn.btnserver.userapp.UserApplicationRepository;
import com.ghostchu.btn.btnserver.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
public class MetricService {
    @Autowired
    private MetricRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserApplicationRepository userApplicationRepository;
    @Autowired
    private MetricGenerator metricGenerator;
    @Value("${metrics.generate_interval}")
    private long generateInterval;

    @Scheduled(fixedRateString = "${metrics.generate_interval}")
    public void summaryMetrics() {
        MetricRecord record = metricGenerator.generate(new Timestamp(Instant.now().minus(generateInterval, ChronoUnit.MILLIS).toEpochMilli()), new Timestamp(System.currentTimeMillis()));
        MetricEntity entity = new MetricEntity();
        entity.setTime(new Timestamp(System.currentTimeMillis()));
        entity.setBannedPeers(record.bannedPeers());
        entity.setBannedBtnPeers(record.bannedBtnPeers());
        entity.setOnlinePeers(record.onlinePeers());
        entity.setOnlineClients(record.onlineClients());
        entity.setRegisteredClients(userApplicationRepository.count());
        entity.setRegisteredUsers(userRepository.count());
        entity = repository.save(entity);
        log.info("统计数据生成完成！记录保存为 ID：{}", entity.getId());
    }

    public List<MetricEntity> fetchMetricsBetween(Timestamp from, Timestamp to) {
        return repository.findByTimeBetween(from, to);
    }

    public MetricEntity fetchLastMetricRecord() {
        return repository.findFirstByOrderByTimeDesc();
    }
}
