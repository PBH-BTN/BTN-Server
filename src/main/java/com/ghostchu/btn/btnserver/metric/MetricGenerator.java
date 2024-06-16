package com.ghostchu.btn.btnserver.metric;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Objects;

@Component
@Slf4j
public class MetricGenerator {
    @Autowired
    @Qualifier("jdbcTemplateClickHouse")
    private JdbcTemplate db;

    public MetricRecord generate(Timestamp from, Timestamp to){
        SimpleDateFormat clickhouseDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fromStr = clickhouseDateFormat.format(from);
        String toStr = clickhouseDateFormat.format(to);
        Long onlineClients = db.queryForObject("SELECT uniqExact(`app_id`) FROM pings WHERE `insert_time` BETWEEN ? AND ?", Long.class, fromStr, toStr);
        Long onlinePeers = db.queryForObject("SELECT uniqExact(tuple(`peer_ip`, `peer_id`)) FROM pings WHERE `insert_time` BETWEEN ? AND ?", Long.class, fromStr, toStr);
        Long bannedPeers = db.queryForObject("SELECT uniqExact(tuple(`peer_ip`, `peer_id`)) FROM bans WHERE `insert_time` BETWEEN ? AND ?", Long.class, fromStr, toStr);
        Long bannedBtnPeers = db.queryForObject("SELECT uniqExact(tuple(`peer_ip`, `peer_id`)) FROM bans WHERE `btn_ban` = TRUE AND `insert_time` BETWEEN ? AND ?", Long.class, fromStr, toStr);
        return new MetricRecord(new Timestamp(System.currentTimeMillis()), Objects.requireNonNullElse(onlineClients, 0L), Objects.requireNonNullElse(onlinePeers, 0L), Objects.requireNonNullElse(bannedPeers, 0L), Objects.requireNonNullElse(bannedBtnPeers, 0L));
    }
}
