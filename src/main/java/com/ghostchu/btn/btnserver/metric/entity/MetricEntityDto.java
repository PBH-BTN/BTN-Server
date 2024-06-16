package com.ghostchu.btn.btnserver.metric.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MetricEntityDto implements Serializable {
    @Positive
    Long id;
    @NotNull
    Timestamp time;
    @PositiveOrZero
    Long onlineClients;
    @PositiveOrZero
    Long onlinePeers;
    @PositiveOrZero
    Long bannedPeers;
    @PositiveOrZero
    Long bannedBtnPeers;
    @PositiveOrZero
    Long registeredUsers;
    @PositiveOrZero
    Long registeredClients;
}
