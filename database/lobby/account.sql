-- 玩家帳號資料

CREATE TABLE `account` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'index',
  `playerId` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '玩家帳號',
  `agentId` varchar(45) COLLATE utf8mb4_bin DEFAULT '' COMMENT '屬於代理商id',
  `account` varchar(45) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '登入帳號',
  `password` varchar(45) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '登入密碼，md5',
  `nickname` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '玩家暱稱',
  `headImage` tinyint DEFAULT '0' COMMENT '頭像圖片編號',
  `sex` tinyint DEFAULT '0' COMMENT '性別，0:未輸入，1:男性，2:女性',
  `coin` float DEFAULT '0' COMMENT '餘額，',
  `deposit` float DEFAULT '0' COMMENT '總儲值金額',
  `withdrawal` float DEFAULT '0' COMMENT '總提取金額',
  `totalBet` float DEFAULT '0' COMMENT '總押注',
  `totalWin` float DEFAULT '0' COMMENT '總贏分',
  `profit` float DEFAULT '0' COMMENT '總利潤',
  `rtp` float DEFAULT '0' COMMENT 'win/bet',
  `lastLoginIP` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '最近一次帳號登錄ip',
  `loginTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最近一次帳號登錄時間',
  `updataTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最近一次帳號資料更新時間',
  `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '帳號創建時間',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`playerId`),
  UNIQUE KEY `account_UNIQUE` (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='玩家帳號資料'