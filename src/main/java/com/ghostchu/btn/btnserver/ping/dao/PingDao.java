package com.ghostchu.btn.btnserver.ping.dao;

import com.ghostchu.btn.btnserver.ping.bean.BtnBan;
import com.ghostchu.btn.btnserver.ping.bean.BtnBanPing;
import com.ghostchu.btn.btnserver.ping.bean.BtnPeer;
import com.ghostchu.btn.btnserver.ping.bean.BtnPeerPing;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Component
public class PingDao {
    @Autowired
    private DataSource db;
    @Autowired
    private JdbcTemplate template;
    @Autowired
    @Qualifier("jdbcTemplateClickHouse")
    private JdbcTemplate clickhouse;

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
                ps.setString(7,  new IPAddressString(con.getIpAddress()).getAddress().toIPv6().toCompressedString());
                ps.setString(8, con.getPeerId()== null ? "未知" : con.getPeerId());
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
                ps.setString(7,  new IPAddressString(con.getIpAddress()).getAddress().toIPv6().toCompressedString());
                ps.setString(8, con.getPeerId()== null ? "未知" : con.getPeerId());
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
