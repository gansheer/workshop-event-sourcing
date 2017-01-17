CREATE DATABASE workshop;

\connect workshop

CREATE TABLE events (
  aggregate_id VARCHAR(128) NOT NULL,
  aggregate_version INT NOT NULL,
  event_class VARCHAR(256) NOT NULL,
  data JSON NOT NULL
);

CREATE UNIQUE INDEX u_events ON events (aggregate_id, aggregate_version);

CREATE TABLE credit_balance (
  bank_account_id VARCHAR(128) NOT NULL,
  value INT NOT NULL
);

CREATE UNIQUE INDEX u_credit_balance ON credit_balance (bank_account_id);
