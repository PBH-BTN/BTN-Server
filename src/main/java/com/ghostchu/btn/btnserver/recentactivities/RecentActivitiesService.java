package com.ghostchu.btn.btnserver.recentactivities;

import com.ghostchu.btn.btnserver.ping.dao.PingDao;
import com.ghostchu.btn.btnserver.util.FormatUtil;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class RecentActivitiesService {
    private final PingDao pingDao;

    public RecentActivitiesService(PingDao pingDao) {
        this.pingDao = pingDao;
    }

    public List<BanActivityDTO> getBanActivities() throws SQLException {
        return pingDao.fetchRecentBans(500).stream().map(ban -> {
            String ip = "???";
            IPAddress ipAddress = new IPAddressString(ban.peerIp().getHostAddress()).getAddress();
            if (ipAddress != null) {
                if (ipAddress.isIPv4Convertible()) {
                    ipAddress = ipAddress.toIPv4();
                }
                ip = ipAddress.toNormalizedString();
            }
            return new BanActivityDTO(
                    ip,
                    ip.length() > 15 ? ip.substring(0, 15)+"[...]" : ip,
                    ban.peerId(),
                    ban.clientName(),
                    ban.torrentIdentifier(),
                    FormatUtil.fileSizeToText(ban.torrentSize()),
                    FormatUtil.fileSizeToText(ban.downloaded()),
                    FormatUtil.fileSizeToText(ban.uploaded()),
                    FormatUtil.fileSizeToText(ban.rtDownloadSpeed()) + "/s",
                    FormatUtil.fileSizeToText(ban.rtUploadSpeed()) + "/s",
                    FormatUtil.formatPercent(ban.progress()),
                    FormatUtil.formatPercent(ban.downloadProgress()),
                    ban.flag(),
                    ban.module(),
                    ban.rule(),
                    ban.rule().length() > 40 ? ban.rule().substring(0, 40) : ban.rule()
            );
        }).toList();
    }
}
