create table incidents
(
    id         uuid primary key,
    project_id uuid         not null references projects (id),
    title      varchar(500) not null,
    hash       varchar(128) not null,
    status     varchar(20)  not null default 'OPEN',
    severity   varchar(20) null,
    ai_summary text null,
    count      int          not null default 1,
    first_seen timestamptz  not null default now(),
    last_seen  timestamptz  not null default now(),
    unique (project_id, hash)
);
