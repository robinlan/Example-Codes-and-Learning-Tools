CREATE TABLE `diary` (
`sn` SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY ,
`diary_date` DATE NOT NULL ,
`diary_content` TEXT NOT NULL ,
INDEX ( `diary_date` )
) TYPE = MYISAM COMMENT = '我的日記本';