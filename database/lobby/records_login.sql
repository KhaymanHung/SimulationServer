-- 帳號登入記錄

CREATE TABLE `records_login` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'index',
  `accountId` varchar(45) COLLATE utf8mb4_bin NOT NULL COMMENT '玩家帳號Id',
  `loginTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '登入時間',
  `logoutTime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '斷線時間',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='帳號登入記錄'