package com.ghostchu.btn.btnserver.ping.bean;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BtnBanPing {
    @JsonProperty("populate_time")
    @NotNull
    private long populateTime;
    @JsonProperty("bans")
    private List<BtnBan> bans;

}
