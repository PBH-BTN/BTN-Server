package com.ghostchu.btn.btnserver.ping.dto;

import java.net.InetAddress;
import java.sql.Timestamp;

public record BtnBanDTO (
        Timestamp populateTime,
        Timestamp insertTime,
        long userId,
        String appId,
        String appSecret,
        String submitId,
        InetAddress peerIp,
        String peerId,
        int peerPort,
        String clientName,
        String torrentIdentifier,
        long torrentSize,
        long downloaded,
        long uploaded,
        long rtDownloadSpeed,
        long rtUploadSpeed,
        double progress,
        double downloadProgress,
        String flag,
        boolean btnBan,
        String module,
        String rule,
        String banUniqueId,
        InetAddress submitterIp
){
}
