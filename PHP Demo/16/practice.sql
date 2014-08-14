-- phpMyAdmin SQL Dump
-- version 2.7.0-pl1
-- http://www.phpmyadmin.net
-- 
-- 主機: localhost
-- 建立日期: Jan 01, 2006, 11:08 PM
-- 伺服器版本: 4.0.24
-- PHP 版本: 4.3.11
-- 
-- 資料庫: `myweb`
-- 

-- --------------------------------------------------------

-- 
-- 資料表格式： `practice`
-- 

CREATE TABLE `practice` (
  `num` tinyint(3) unsigned NOT NULL auto_increment,
  `name` varchar(255) NOT NULL default '',
  `sex` enum('男','女') NOT NULL default '女',
  `birthday` date NOT NULL default '0000-00-00',
  `salary` smallint(5) unsigned NOT NULL default '0',
  PRIMARY KEY  (`num`)
) TYPE=MyISAM PACK_KEYS=0 AUTO_INCREMENT=7 ;

-- 
-- 列出以下資料庫的數據： `practice`
-- 

INSERT INTO `practice` VALUES (1, 'tad', '男', '1973-06-16', 35000);
INSERT INTO `practice` VALUES (2, 'apple', '男', '1973-06-10', 25000);
INSERT INTO `practice` VALUES (3, 'tim', '男', '1972-01-10', 50000);
INSERT INTO `practice` VALUES (4, 'huihui', '女', '1980-03-07', 40000);
INSERT INTO `practice` VALUES (5, 'alice', '女', '1976-12-01', 35000);
INSERT INTO `practice` VALUES (6, 'phebe', '女', '1973-03-10', 60000);
