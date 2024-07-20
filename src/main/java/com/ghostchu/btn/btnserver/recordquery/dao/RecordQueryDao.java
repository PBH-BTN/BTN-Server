package com.ghostchu.btn.btnserver.recordquery.dao;

import com.ghostchu.btn.btnserver.ping.dao.PingDao;
import com.ghostchu.btn.btnserver.ping.dto.BtnBanDTO;
import com.ghostchu.btn.btnserver.ping.dto.BtnPingDTO;
import com.ghostchu.btn.btnserver.recordquery.bean.QueryResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecordQueryDao {
    @Autowired
    @Qualifier("jdbcTemplateClickHouse")
    private JdbcTemplate clickhouse;

    public List<QueryResultDTO> queryRecords(
            String ip,
            String peerId,
            String clientName,
            boolean onlyBans,
            String connector,
            long offset,
            long limit
    ){
        String banSQL = "SELECT *, replaceRegexpOne(IPv6NumToString(peer_ip), '^::ffff:', '') AS ip FROM bans WHERE ip LIKE ? %s peer_id LIKE ? %s client_name LIKE ? LIMIT "+offset+","+limit;
        banSQL = String.format(banSQL, connector, connector);
        String pingSQL = "SELECT *, replaceRegexpOne(IPv6NumToString(peer_ip), '^::ffff:', '') AS ip FROM pings WHERE ip LIKE ? %s peer_id LIKE ? %s client_name LIKE ? LIMIT "+offset+","+limit;
        pingSQL = String.format(pingSQL, connector, connector);
        @SuppressWarnings("SqlSourceToSinkFlow")
        List<BtnBanDTO> bans = clickhouse.query(banSQL,new PingDao.BanRowMapper(),ip, peerId, clientName);
        @SuppressWarnings("SqlSourceToSinkFlow")
        List<BtnPingDTO> pings = clickhouse.query(pingSQL, new PingDao.PingRowMapper(),ip, peerId, clientName);
        List<QueryResultDTO> queries = new ArrayList<>();
        queries.addAll(bans.stream().map(QueryResultDTO::from).toList());
        if(!onlyBans) {
            queries.addAll(pings.stream().map(QueryResultDTO::from).toList());
        }
        queries.sort((o1, o2) -> o2.insertAt().compareTo(o1.insertAt()));
        return queries;
    }

}
