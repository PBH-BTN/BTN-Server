package com.ghostchu.btn.btnserver.metric.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MetricBasicBean {
    @JsonProperty("hour_start")
    private Timestamp hourStart;
    @JsonProperty("summary_btn_users")
    private long summaryBtnUsers;
    @JsonProperty("summary_torrents")
    private long summaryTorrents;
    @JsonProperty("summary_peers")
    private long summaryPeers;
    @JsonProperty("summary_submit_req")
    private long summarySubmitReq;
    @JsonProperty("summary_peerid")
    private long summaryPeerid;
    @JsonProperty("summary_clientname")
    private long summaryClientname;
    @JsonProperty("summary_bans")
    private long summaryBans;
}
