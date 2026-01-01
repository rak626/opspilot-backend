create table logs
(
    id           uuid primary key,
    incident_id  uuid        not null references incidents (id),
    payload_json jsonb       not null,
    created_at   timestamptz not null default now()
);

create index idx_logs_incident_id on logs (incident_id);
create index idx_logs_created_at on logs (created_at);
