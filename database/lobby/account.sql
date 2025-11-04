-- 玩家帳號資料

-- MySQL dump 10.13  Distrib 8.0.43, for Win64 (x86_64)
--
-- Host: 192.168.1.177    Database: lobby
-- ------------------------------------------------------
-- Server version	9.5.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
SET @MYSQLDUMP_TEMP_LOG_BIN = @@SESSION.SQL_LOG_BIN;
SET @@SESSION.SQL_LOG_BIN= 0;

--
-- GTID state at the beginning of the backup 
--

SET @@GLOBAL.GTID_PURGED=/*!80000 '+'*/ '60f920fe-b541-11f0-af1d-0242ac110002:1-8';

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'index',
  `account` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '登入帳號',
  `password` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '登入密碼，md5',
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
  UNIQUE KEY `account_UNIQUE` (`account`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='玩家帳號資料';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,'admin1','lfl1234','admin1',1,0,5000,0,0,0,0,0,0,'','2025-10-30 05:09:01','2025-10-30 05:09:01','2025-10-30 05:09:01'),(2,'admin2','lfl1234','admin2',2,0,5000,0,0,0,0,0,0,'','2025-10-30 05:09:01','2025-10-30 05:09:01','2025-10-30 05:09:01'),(3,'admin3','lfl1234','admin3',3,0,5000,0,0,0,0,0,0,'','2025-10-30 05:09:01','2025-10-30 05:09:01','2025-10-30 05:09:01'),(4,'admin4','lfl1234','admin4',4,0,5000,0,0,0,0,0,0,'','2025-10-30 05:09:01','2025-10-30 05:09:01','2025-10-30 05:09:01'),(5,'admin5','lfl1234','admin5',5,0,5000,0,0,0,0,0,0,'','2025-10-30 05:09:01','2025-10-30 05:09:01','2025-10-30 05:09:01'),(6,'admin6','lfl1234','admin6',6,0,5000,0,0,0,0,0,0,'','2025-10-30 05:09:01','2025-10-30 05:09:01','2025-10-30 05:09:01'),(7,'admin7','lfl1234','admin7',7,0,5000,0,0,0,0,0,0,'','2025-10-30 05:09:01','2025-10-30 05:09:01','2025-10-30 05:09:01'),(8,'admin8','lfl1234','admin8',8,0,5000,0,0,0,0,0,0,'','2025-10-30 05:09:01','2025-10-30 05:09:01','2025-10-30 05:09:01'),(9,'admin9','lfl1234','admin9',9,0,5000,0,0,0,0,0,0,'','2025-10-30 05:09:01','2025-10-30 05:09:01','2025-10-30 05:09:01'),(10,'admin10','lfl1234','admin10',10,0,5000,0,0,0,0,0,0,'','2025-10-30 05:09:01','2025-10-30 05:09:01','2025-10-30 05:09:01');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;
SET @@SESSION.SQL_LOG_BIN = @MYSQLDUMP_TEMP_LOG_BIN;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-10-30 13:10:29

