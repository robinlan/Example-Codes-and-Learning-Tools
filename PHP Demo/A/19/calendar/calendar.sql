CREATE TABLE calendar (
  sn smallint(5) unsigned NOT NULL auto_increment,
  start_time datetime NOT NULL default '0000-00-00 00:00:00',
  end_time datetime NOT NULL default '0000-00-00 00:00:00',
  event_title varchar(255) NOT NULL default '',
  event_content text NOT NULL,
  PRIMARY KEY  (sn),
  KEY diary_date (start_time)
) TYPE=MyISAM COMMENT='我的行事曆';
