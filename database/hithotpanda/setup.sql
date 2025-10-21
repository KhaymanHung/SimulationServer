-- 遊戲設定

CREATE TABLE `setup` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'index',
  `gameId` varchar(45) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '遊戲Id',
  `betLv` int DEFAULT NULL COMMENT '押注等級',
  `betScores` text COLLATE utf8mb4_bin COMMENT '押注選項',
  `multiple` int DEFAULT NULL COMMENT '倍數',
  `lines` int DEFAULT NULL COMMENT '線數',
  `minBet` int DEFAULT NULL COMMENT '最低押注金額',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='遊戲設定'

INSERT INTO `hithotpanda`.`setup` (`gameId`, `betLv`, `betScores`, `multiple`, `lines`, `minBet`) VALUES ('8866020', '1', '[1,10,40,200]', '100', '30', '1');
