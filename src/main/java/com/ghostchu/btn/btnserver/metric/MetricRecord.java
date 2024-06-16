package com.ghostchu.btn.btnserver.metric;

import java.sql.Timestamp;

public record MetricRecord(
        Timestamp time,
        Long onlineClients,
        Long onlinePeers,
        Long bannedPeers,
        Long bannedBtnPeers
) {
}
