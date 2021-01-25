drop table if exists USER;

create table USER(
  ID int not null AUTO_INCREMENT,
  E_MAIL varchar(100) not null,
  PRIMARY KEY ( ID )
);