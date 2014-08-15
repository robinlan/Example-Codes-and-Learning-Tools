drop table adinfo;
drop table user;
drop table rgroup;
drop table resource;
drop table olist;
drop table oinfo;

create table adinfo
( 
 adname varchar(10) primary key,
 adpwd varchar(20) not null,
 adlevel int not null
);

create table user
(
  uname  varchar(20) PRIMARY KEY,
  pwd varchar(20) NOT NULL,
  telNum varchar(20) NOT NULL,
  realName varchar(10),
  gender char(2) DEFAULT '男',
  email  varchar(40)  NOT NULL
);

create table rgroup
(
 gId int primary key,
 gName varchar(10),
 gImg varchar(40),
 gDetail varchar(200) not null,
 gOrderDet varchar(300) not null
);

create table resource
(
  rid int,
  rgroup int references rgroup(gid),
  rgid varchar(10),
  rlevel varchar(10) not null,
  rmoney double not null,
  rdetail varchar(100),
  rstatus varchar(10) default '空閒',
  primary key(rid,rgid)
);

create table olist
(
oid   int primary key,
oname varchar(20) not null,
otime varchar(20) not null,
odeal varchar(20) default '無',
ostatus varchar(10) default '預訂中',
oreason varchar(100) default '無'
);

create table oinfo
(
  orid int primary key,
  oid  int references olist(oid),
  rgid   varchar(10) references resource(rgid),
  ftime varchar(20) not null,
  etime varchar(20) not null,
  ostatus varchar(10) not null default '預定中'
);
