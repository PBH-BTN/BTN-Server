package com.ghostchu.btn.btnserver.rule;

import com.ghostchu.btn.btnserver.ping.dao.PingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class RuleService {
    @Autowired
    private RuleRepository repository;
    @Autowired
    private PingDao pingDao;

    public List<RuleEntity> readRules(long timestamp) {
        return new ArrayList<>(repository.findByExpireAtLessThanEqual(timestamp));
    }

    public List<RuleEntity> readUntrustedRules(int having) {
        return pingDao.fetchUntrustedBans(having).stream()
                .map(banEntry->new RuleEntity("BTN-自动生成-不受信任IP", "ip", banEntry, 0L, Long.MAX_VALUE))
                .toList();
    }

    public List<RuleEntity> readRulesIncludeExpired() {
        List<RuleEntity> ruleEntities = new ArrayList<>();
        repository.findAll().forEach(ruleEntities::add);
        return ruleEntities;
    }
}
