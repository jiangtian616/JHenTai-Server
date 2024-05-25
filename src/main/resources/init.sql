create table jhentai.config
(
    id                  bigint auto_increment
        primary key,
    share_code          varchar(256)                        not null comment 'unique code for share',
    identification_code varchar(256)                        not null comment 'user''s identification',
    type                int       default 0                 not null comment '0:all',
    version             varchar(20)                         not null,
    config              text                                null,
    is_deleted          tinyint   default 0                 not null,
    ctime               timestamp default CURRENT_TIMESTAMP not null comment 'creation time',
    utime               timestamp default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment 'update time',
    constraint uniq_share_code
        unique (share_code)
);

create index idx_identification_code
    on jhentai.config (identification_code);

create index idx_identification_code_ctime
    on jhentai.config (identification_code, ctime);

create index idx_identification_code_type
    on jhentai.config (identification_code, type);

