CREATE TABLE pings
(
    `populate_time` DateTime CODEC(ZSTD(4)),
    `insert_time` DateTime CODEC(ZSTD(4)),
    `user_id` UInt32,
    `app_id` String CODEC(ZSTD(4)),
    `app_secret` String CODEC(ZSTD(4)),
    `submit_id` String CODEC(ZSTD(4)),
    `peer_ip` Nullable(IPv6) CODEC(ZSTD(4)),
    `peer_id` String CODEC(ZSTD(4)),
    `peer_port` UInt16,
    `client_name` Nullable(String) CODEC(ZSTD(4)),
    `torrent_identifier` String CODEC(ZSTD(4)),
    `torrent_size` UInt64,
    `downloaded` Int64,
    `rt_download_speed` Int64,
    `uploaded` Int64,
    `rt_upload_speed` Int64,
    `progress` Float32,
    `downloader_progress` Float32,
    `flag` String CODEC(ZSTD(4)),
    `submitter_ip` Nullable(IPv6) CODEC(ZSTD(4))
)
    ENGINE = MergeTree()
    ORDER BY (populate_time,app_id,uploaded)
SETTINGS index_granularity = 8192;

CREATE TABLE bans
(
    `populate_time` DateTime CODEC(ZSTD(4)),
    `insert_time` DateTime CODEC(ZSTD(4)),
    `user_id` UInt32,
    `app_id` String CODEC(ZSTD(4)),
    `app_secret` String CODEC(ZSTD(4)),
    `submit_id` String CODEC(ZSTD(4)),
    `peer_ip` Nullable(IPv6) CODEC(ZSTD(4)),
    `peer_id` String CODEC(ZSTD(4)),
    `peer_port` UInt16,
    `client_name` Nullable(String) CODEC(ZSTD(4)),
    `torrent_identifier` String CODEC(ZSTD(4)),
    `torrent_size` UInt64,
    `downloaded` Int64,
    `rt_download_speed` Int64,
    `uploaded` Int64,
    `rt_upload_speed` Int64,
    `progress` Float32,
    `downloader_progress` Float32,
    `flag` String CODEC(ZSTD(4)),
    `btn_ban` BOOLEAN,
    `module` Nullable(String) CODEC(ZSTD(4)),
    `rule` Nullable(String) CODEC(ZSTD(4)),
    `ban_unique_id` String CODEC(ZSTD(4)),
    `submitter_ip` Nullable(IPv6) CODEC(ZSTD(4))
)
    ENGINE = ReplacingMergeTree(populate_time)
    ORDER BY (populate_time,app_id,ban_unique_id)
SETTINGS index_granularity = 8192;