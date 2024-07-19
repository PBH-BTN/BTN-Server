package com.ghostchu.btn.btnserver.ping.dao;

import com.ghostchu.btn.btnserver.ping.bean.BtnBan;
import com.ghostchu.btn.btnserver.ping.bean.BtnBanPing;
import com.ghostchu.btn.btnserver.ping.bean.BtnPeer;
import com.ghostchu.btn.btnserver.ping.bean.BtnPeerPing;
import com.ghostchu.btn.btnserver.ping.dto.BtnBanDTO;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.net.InetAddress;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PingDao {
    @Autowired
    private DataSource db;
    @Autowired
    private JdbcTemplate template;
    @Autowired
    @Qualifier("jdbcTemplateClickHouse")
    private JdbcTemplate clickhouse;

    public List<BtnBanDTO> fetchRecentBans(int size) throws SQLException {
        List<BtnBanDTO> list = new ArrayList<>();
        List<Map<String, Object>> results = clickhouse.queryForList("SELECT * FROM bans LIMIT " + size);
        results.forEach(map -> {

            list.add(new BtnBanDTO(
                    (LocalDateTime) map.get("populate_time"),
                    (LocalDateTime) map.get("insert_time"),
                    ((Number) map.get("user_id")).intValue(),
                    (String) map.get("app_id"),
                    (String) map.get("app_secret"),
                    (String) map.get("submit_id"),
                    (InetAddress) map.get("peer_ip"),
                    (String) map.get("peer_id"),
                    ((Number) map.get("peer_port")).intValue(),
                    (String) map.get("client_name"),
                    (String) map.get("torrent_identifier"),
                    ((Number) map.get("torrent_size")).longValue(),
                    ((Number) map.get("downloaded")).longValue(),
                    ((Number) map.get("uploaded")).longValue(),
                    ((Number) map.get("rt_download_speed")).longValue(),
                    ((Number) map.get("rt_upload_speed")).longValue(),
                    ((Number) map.get("progress")).doubleValue(),
                    ((Number) map.get("downloader_progress")).doubleValue(),
                    (String) map.get("flag"),
                    (Boolean) map.get("btn_ban"),
                    (String) map.get("module"),
                    (String) map.get("rule"),
                    (String) map.get("ban_unique_id"),
                    (InetAddress) map.get("submitter_ip")
            ));
        });
        return list;
    }

    public void insertBan(BtnBanPing ping, IPAddress submitterAddress, long userId, String appId, String appSecret, String submitId) {
        List<BtnBan> connections = ping.getBans();
        clickhouse.batchUpdate("INSERT INTO bans (populate_time, insert_time, user_id, app_id, app_secret, submit_id, " +
                "peer_ip, peer_id, peer_port, client_name, torrent_identifier, torrent_size," +
                "downloaded, rt_download_speed, uploaded, rt_upload_speed, progress, downloader_progress, flag, btn_ban, " +
                "module, rule, ban_unique_id, submitter_ip)" +
                " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                BtnBan ban = connections.get(i);
                BtnPeer con = ban.getPeer();
                ps.setTimestamp(1, new Timestamp(ping.getPopulateTime()));
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                ps.setLong(3, userId);
                ps.setString(4, appId);
                ps.setString(5, appSecret);
                ps.setString(6, submitId);
                ps.setString(7, new IPAddressString(con.getIpAddress()).getAddress().toIPv6().toCompressedString());
                ps.setString(8, con.getPeerId() == null ? "未知" : con.getPeerId());
                ps.setLong(9, con.getPeerPort());
                ps.setString(10, con.getClientName() == null ? "未知" : con.getClientName());
                ps.setString(11, con.getTorrentIdentifier());
                ps.setLong(12, con.getTorrentSize());
                ps.setLong(13, con.getDownloaded());
                ps.setLong(14, con.getRtDownloadSpeed());
                ps.setLong(15, con.getUploaded());
                ps.setLong(16, con.getRtUploadSpeed());
                ps.setDouble(17, con.getPeerProgress());
                ps.setDouble(18, con.getDownloaderProgress());
                ps.setString(19, con.getPeerFlag());
                ps.setBoolean(20, ban.isBtnBan());
                ps.setString(21, ban.getModule());
                ps.setString(22, ban.getRule());
                ps.setString(23, ban.getBanUniqueId());
                ps.setString(24, submitterAddress.toString());
            }

            @Override
            public int getBatchSize() {
                return connections.size();
            }
        });
    }

    public void insertPing(BtnPeerPing ping, IPAddress submitterAddress, long userId, String appId, String appSecret, String submitId) {
        List<BtnPeer> connections = ping.getPeers();
        clickhouse.batchUpdate("INSERT INTO pings (populate_time, insert_time, user_id, app_id, app_secret, submit_id, " +
                "peer_ip, peer_id, peer_port, client_name, torrent_identifier, torrent_size," +
                "downloaded, rt_download_speed, uploaded, rt_upload_speed, progress, downloader_progress, flag, submitter_ip ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
                BtnPeer con = connections.get(i);
                ps.setTimestamp(1, new Timestamp(ping.getPopulateTime()));
                ps.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
                ps.setLong(3, userId);
                ps.setString(4, appId);
                ps.setString(5, appSecret);
                ps.setString(6, submitId);
                ps.setString(7, new IPAddressString(con.getIpAddress()).getAddress().toIPv6().toCompressedString());
                ps.setString(8, con.getPeerId() == null ? "未知" : con.getPeerId());
                ps.setLong(9, con.getPeerPort());
                ps.setString(10, con.getClientName() == null ? "未知" : con.getClientName());
                ps.setString(11, con.getTorrentIdentifier());
                ps.setLong(12, con.getTorrentSize());
                ps.setLong(13, con.getDownloaded());
                ps.setLong(14, con.getRtDownloadSpeed());
                ps.setLong(15, con.getUploaded());
                ps.setLong(16, con.getRtUploadSpeed());
                ps.setDouble(17, con.getPeerProgress());
                ps.setDouble(18, con.getDownloaderProgress());
                ps.setString(19, con.getPeerFlag());
                ps.setString(20, submitterAddress.toString());
            }

            @Override
            public int getBatchSize() {
                return connections.size();
            }
        });
    }

}
