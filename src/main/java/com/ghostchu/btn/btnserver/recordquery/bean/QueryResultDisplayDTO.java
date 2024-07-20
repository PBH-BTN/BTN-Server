package com.ghostchu.btn.btnserver.recordquery.bean;

import com.ghostchu.btn.btnserver.util.FormatUtil;

public record QueryResultDisplayDTO(
        String insertAt,
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
) {
    public static QueryResultDisplayDTO from(QueryResultDTO dto) {
        return new QueryResultDisplayDTO(
                FormatUtil.formatDate(dto.insertAt()),
                dto.address(),
                dto.address().length() > 15 ? dto.address().substring(0, 15) + "[...]" : dto.address(),
                dto.peerId(),
                dto.clientName(),
                dto.torrentIdentifier(),
                FormatUtil.fileSizeToText(dto.torrentSize()),
                FormatUtil.fileSizeToText(dto.downloaded()),
                FormatUtil.fileSizeToText(dto.uploaded()),
                FormatUtil.fileSizeToText(dto.rtDownloadedSpeed()) + "/s",
                FormatUtil.fileSizeToText(dto.rtUploadedSpeed()) + "/s",
                FormatUtil.formatPercent(dto.progress()),
                FormatUtil.formatPercent(dto.downloadProgress()),
                dto.flags(),
                dto.module(),
                dto.rule(),
                dto.rule().length() > 40 ? dto.rule().substring(0, 40) : dto.rule()
        );
    }

}
