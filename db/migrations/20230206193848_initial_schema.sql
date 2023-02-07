-- +goose Up
-- +goose StatementBegin

create sequence hibernate_sequence;

create table standup (
    id bigint primary key,
    name text unique not null
);

create table app_user (
    id bigint primary key,
    username text unique not null
);

create table standup_user (
    id bigint primary key,
    standup_id bigint references standup not null,
    user_id bigint references app_user not null
);

-- +goose StatementEnd

-- +goose Down
-- +goose StatementBegin

drop table standup_user;
drop table app_user;
drop table standup;
drop sequence hibernate_sequence;

-- +goose StatementEnd

