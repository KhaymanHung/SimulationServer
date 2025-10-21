-- 遊戲數值統計(每日)

CREATE TABLE `records_game` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'index',
  `date` varchar(45) COLLATE utf8mb4_bin NOT NULL DEFAULT '1900-01-01' COMMENT '日期',
  `gameId` varchar(45) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '遊戲Id，同gamelist內的gameid',
  `totalBet` float DEFAULT '0' COMMENT '總押注',
  `totalWin` float DEFAULT '0' COMMENT '總贏分',
  `profit` float DEFAULT '0' COMMENT '總利潤',
  `rtp` float DEFAULT '0' COMMENT 'win/bet',
  `maxPlayer` int DEFAULT '0' COMMENT '同時最多玩家數',
  `updataTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次更新時間',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='遊戲數值統計(每日)'