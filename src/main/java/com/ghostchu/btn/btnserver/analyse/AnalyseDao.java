package com.ghostchu.btn.btnserver.analyse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class AnalyseDao {
    @Autowired
    @Qualifier("jdbcTemplateClickHouse")
    private JdbcTemplate clickhouse;

    // 分析 ProgressChecker 日志
    // SELECT populate_time ,app_id ,torrent_identifier ,replaceRegexpOne(IPv6NumToString(peer_ip), '^::ffff:', '') as peer_ip , client_name , peer_id , rule FROM btn3.bans WHERE module = 'com.ghostchu.peerbanhelper.module.impl.rule.ProgressCheatBlocker' ORDER BY peer_ip

    // BT 客户端 ClientName 计数
    // SELECT client_name , COUNT(client_name) AS count FROM btn3.pings GROUP BY client_name  ORDER BY count DESC

    // 检查每个 IP 下独立客户端数量
    /*
    SELECT
    replaceRegexpOne(IPv6NumToString(peer_ip), '^::ffff:', '') AS peer_ip,
    COUNT(DISTINCT (peer_id, client_name)) AS pair_count
FROM
    btn3.pings
GROUP BY
    peer_ip
HAVING
    pair_count > 1
ORDER BY
    pair_count DESC;



     */
    // 反吸血检查
    /*
    SELECT DISTINCT  peer_ip
FROM btn3.pings
WHERE
    flag LIKE '%d%'
    AND downloader_progress != 1
    AND uploaded > 500
ORDER BY
    peer_ip ASC;
     */


}
