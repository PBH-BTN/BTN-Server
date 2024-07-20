package com.ghostchu.btn.btnserver.recordquery.bean;

import com.ghostchu.btn.btnserver.ping.dto.BtnBanDTO;
import com.ghostchu.btn.btnserver.ping.dto.BtnPingDTO;
import inet.ipaddr.IPAddress;
import inet.ipaddr.IPAddressString;

import java.sql.Timestamp;

public record QueryResultDTO(
        Timestamp insertAt,
        String address,
        String peerId,
        String clientName,
        String torrentIdentifier,
        long torrentSize,
        long downloaded,
        long uploaded,
        long rtDownloadedSpeed,
        long rtUploadedSpeed,
        double progress,
        double downloadProgress,
        String flags,
        String module,
        String rule
) {
    public static QueryResultDTO from(BtnBanDTO banDTO) {
        String ip = "???";
        IPAddress ipAddress = new IPAddressString(banDTO.peerIp().getHostAddress()).getAddress();
        if (ipAddress != null) {
            if (ipAddress.isIPv4Convertible()) {
                ipAddress = ipAddress.toIPv4();
            }
            ip = ipAddress.toNormalizedString();
        }
        return new QueryResultDTO(
                banDTO.insertTime(),
                ip,
                banDTO.peerId(),
                banDTO.clientName(),
                banDTO.torrentIdentifier(),
                banDTO.torrentSize(),
                banDTO.downloaded(),
                banDTO.uploaded(),
                banDTO.rtDownloadSpeed(),
                banDTO.rtUploadSpeed(),
                banDTO.progress(),
                banDTO.downloadProgress(),
                banDTO.flag(),
                banDTO.module(),
                banDTO.rule()
        );
    }

    public static QueryResultDTO from(BtnPingDTO pingDTO) {
        String ip = "???";
        IPAddress ipAddress = new IPAddressString(pingDTO.peerIp().getHostAddress()).getAddress();
        if (ipAddress != null) {
            if (ipAddress.isIPv4Convertible()) {
                ipAddress = ipAddress.toIPv4();
            }
            ip = ipAddress.toNormalizedString();
        }
        return new QueryResultDTO(
                pingDTO.insertTime(),
                ip,
                pingDTO.peerId(),
                pingDTO.clientName(),
                pingDTO.torrentIdentifier(),
                pingDTO.torrentSize(),
                pingDTO.downloaded(),
                pingDTO.uploaded(),
                pingDTO.rtDownloadSpeed(),
                pingDTO.rtUploadSpeed(),
                pingDTO.progress(),
                pingDTO.downloadProgress(),
                pingDTO.flag(),
                "",
                ""
        );
    }

}
