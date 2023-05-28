-- MySQL dump 10.13  Distrib 8.0.12, for Linux (x86_64)
--
-- Host: localhost    Database: et_decoder
-- ------------------------------------------------------
-- Server version	8.0.12

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8mb4 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `t_etbase_camera`
--

DROP TABLE IF EXISTS `t_etbase_camera`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_etbase_camera` (
  `decoder_id` bigint(20) unsigned NOT NULL COMMENT '主键',
  `camera_status` varchar(2) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL DEFAULT '1' COMMENT '摄像头状态',
  `model` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '型号',
  `mac` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'mac地址',
  `index_x` double DEFAULT '0' COMMENT 'x轴',
  `index_y` double DEFAULT '0' COMMENT 'y轴',
  `index_z` double DEFAULT '0' COMMENT 'z轴',
  `map_id` int(11) DEFAULT NULL COMMENT '所属地图',
  `ip` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '解码器ip',
  `user_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `port` int(255) NOT NULL DEFAULT '554' COMMENT '端口',
  PRIMARY KEY (`decoder_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_etbase_camera`
--

LOCK TABLES `t_etbase_camera` WRITE;
/*!40000 ALTER TABLE `t_etbase_camera` DISABLE KEYS */;
INSERT INTO `t_etbase_camera` VALUES (2,'1','CS-C3T-1B2ER','98-8b-0a-9d-59-a0',2.1,1.6,1,1,'192.168.10.109','admin','FQZEVT',554),(3,'1','CS-C3T-1B2ER','98-8B-0A-9D-59-C4',1,2,1,1,'192.168.10.107','admin','ZTNEDK',554),(4,'1','CS-C3T-1B2ER','68-6D-BC-85-91-E1',1,1,1,1,'192.168.1.139','admin','MJXPGO',554);
/*!40000 ALTER TABLE `t_etbase_camera` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `t_user_decoder`
--

DROP TABLE IF EXISTS `t_user_decoder`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `t_user_decoder` (
  `id` bigint(20) unsigned NOT NULL,
  `decoder_id` bigint(20) NOT NULL COMMENT '解码器ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `protocol` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原协议',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '名字',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态',
  `model` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'etbase' COMMENT '设备型号可有可无',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `t_user_decoder`
--

LOCK TABLES `t_user_decoder` WRITE;
/*!40000 ALTER TABLE `t_user_decoder` DISABLE KEYS */;
INSERT INTO `t_user_decoder` VALUES (1,2,1,'RTSP','iwejf',1,''),(2,1,2347362,'RTSP','sfgg',1,'etbase'),(3,3,1,'RTSP','eijtoij',1,'etbase'),(4,4,1,'RTSP','ASDASD',1,'etbase');
/*!40000 ALTER TABLE `t_user_decoder` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-08-20 15:22:39
