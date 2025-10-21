-- 黑名單

CREATE TABLE `blocklist` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'index',
  `ip` varchar(45) COLLATE utf8mb4_bin NOT NULL COMMENT 'ip',
  `note` varchar(255) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '備注',
  `updateTime` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `createTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '創建時間',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_UNIQUE` (`id`),
  UNIQUE KEY `ip_UNIQUE` (`ip`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='黑名單'