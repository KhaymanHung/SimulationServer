-- 遊戲列表

CREATE TABLE `gamelist` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'index',
  `gameid` varchar(45) COLLATE utf8mb4_bin NOT NULL COMMENT '遊戲編號',
  `gameName` varchar(45) COLLATE utf8mb4_bin DEFAULT '' COMMENT '遊戲名稱(中文)',
  `level` tinyint(1) DEFAULT '0' COMMENT '登入權限',
  `stats` tinyint(1) DEFAULT '0' COMMENT '遊戲狀態，0:關閉，1:開啟，2:維護，3:不對外開啟，預設為0',
  `totalBet` float DEFAULT '0' COMMENT '總押注',
  `totalWin` float DEFAULT '0' COMMENT '總贏分',
  `profit` float DEFAULT '0' COMMENT '總利潤',
  `rtp` float DEFAULT '0' COMMENT 'win/bet',
  `maxPlayer` int DEFAULT '0' COMMENT '同時最多玩家數',
  `updataTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次更新時間',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `gameid_UNIQUE` (`gameid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='遊戲列表'