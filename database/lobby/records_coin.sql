-- 玩家存提款記錄

CREATE TABLE `records_coin` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'index',
  `playerId` varchar(45) COLLATE utf8mb4_bin NOT NULL COMMENT '對應lobby.account.id',
  `deposit` float DEFAULT '0' COMMENT '儲值金額',
  `withdrawal` float DEFAULT '0' COMMENT '提取金額',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='玩家存提款記錄'