DROP TABLE IF EXISTS `cc_citizen`.`lookup_segment_code`;

CREATE TABLE  `cc_citizen`.`lookup_segment_code` (
  `sk` varchar(255) NOT NULL default'',
  `code` varchar(45) NOT NULL default '',
  `description` varchar(45) NOT NULL default'',
  `rank` int(11) NOT NULL default 0,
  `memo` boolean NOT NULL default false,
  `color` int(11) NOT NULL default 0,
  `font_color` int(11) NOT NULL default 0,
  `default_duration` int(11) NOT NULL default 0,
  `updated_by_sk` varchar(45) NOT NULL default'',
  `updated_on` varchar(45) NOT NULL default '',
  PRIMARY KEY  (`sk`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;