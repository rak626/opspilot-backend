create table projects
(
    id         uuid primary key,
    name       varchar(255) not null,
    public_key varchar(255) not null unique,
    created_at timestamptz  not null default now()
);
