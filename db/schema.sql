begin;

create table standup (
        id bigserial primary key,
        name text unique not null
);

create table app_user (
        id bigserial primary key,
        username text unique not null
);

create table standup_user (
        id bigserial primary key,
        standup_id bigint references standup not null,
        user_id bigint references users not null
);

commit;
