-- 代理商存提款記錄(每日)

CREATE TABLE `records_agent` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'index',
  `date` varchar(45) COLLATE utf8mb4_bin NOT NULL DEFAULT '1990-01-01' COMMENT '統計日期',
  `agentId` varchar(45) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '代理商id',
  `deposit` float DEFAULT '0' COMMENT '總儲值金額',
  `withdrawal` float DEFAULT '0' COMMENT '總提取金額',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新時間',
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='代理商存提款記錄(每日)'