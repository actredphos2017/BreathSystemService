drop database if exists BreathSystem;
create database if not exists BreathSystem;
use BreathSystem;

create table accounts
(
    name varchar(30) not null,
    id varchar(30) not null,
    password varchar(64) not null,
    equipmentIdentificationCode varchar(64) default null,
    index (id),
    primary key (id)
) engine = InnoDB
  default charset = utf8;