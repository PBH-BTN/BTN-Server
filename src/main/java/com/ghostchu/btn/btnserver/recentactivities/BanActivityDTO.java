package com.ghostchu.btn.btnserver.recentactivities;

public record BanActivityDTO(
        String address,
        String addressCutted,
        String peerId,
        String clientName,
        String torrentIdentifier,
        String torrentSize,
        String downloaded,
        String uploaded,
        String rtDownloadedSpeed,
        String rtUploadedSpeed,
        String progress,
        String downloadProgress,
        String flags,
        String module,
        String rule,
        String ruleCutted
){

}
