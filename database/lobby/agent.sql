-- 代理商

CREATE TABLE `agent` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'index',
  `agentId` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '代理商id',
  `name` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '代理商名稱',
  `playerCount` int DEFAULT '0' COMMENT '玩家帳號數',
  `deposit` float DEFAULT '0' COMMENT '總儲值金額',
  `withdrawal` float DEFAULT '0' COMMENT '總提取金額',
  `totalBet` float DEFAULT '0' COMMENT '總押注',
  `totalWin` float DEFAULT '0' COMMENT '總贏分',
  `profit` float DEFAULT '0' COMMENT '總利潤',
  `rtp` float DEFAULT '0' COMMENT 'win/bet',
  `gamelist` text COLLATE utf8mb4_bin COMMENT '可登入遊戲列表',
  `updataTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次更新時間',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `agentId_UNIQUE` (`agentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='代理商'