-- 遊戲歷程

CREATE TABLE `records_game` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'index',
  `sid` varchar(45) COLLATE utf8mb4_bin NOT NULL COMMENT '單round局號，第1 round時與psid相同',
  `psid` varchar(45) COLLATE utf8mb4_bin NOT NULL COMMENT '主局號',
  `playerId` varchar(45) COLLATE utf8mb4_bin NOT NULL COMMENT '玩家id',
  `details` text COLLATE utf8mb4_bin COMMENT '遊戲詳細內容',
  `time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '遊戲時間',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='遊戲歷程'