-- MySQL dump 10.13  Distrib 5.6.50, for Linux (x86_64)
--
-- Host: localhost    Database: corpusrole
-- ------------------------------------------------------
-- Server version	5.6.50-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admins`
--

DROP TABLE IF EXISTS `admins`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admins` (
  `aid` int(11) NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `anumber` varchar(255) NOT NULL COMMENT '管理员工号',
  `admname` varchar(255) NOT NULL COMMENT '管理员姓名',
  `admpassword` varchar(255) NOT NULL COMMENT '管理员密码',
  `asex` int(11) NOT NULL COMMENT '管理员性别（1男，0女）',
  PRIMARY KEY (`aid`,`anumber`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admins`
--

LOCK TABLES `admins` WRITE;
/*!40000 ALTER TABLE `admins` DISABLE KEYS */;
INSERT INTO `admins` VALUES (5,'1001','root004','$2a$10$R8QJvj0VIQDnAdpRLRXFEeYubLJplF5M2K6/AI/8dZORKecLMmjIS',0),(6,'1002','root002','$2a$10$R8QJvj0VIQDnAdpRLRXFEeYubLJplF5M2K6/AI/8dZORKecLMmjIS',1),(7,'123','管理员002-测试','123456',0);
/*!40000 ALTER TABLE `admins` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `classes`
--

DROP TABLE IF EXISTS `classes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `classes` (
  `cid` int(11) NOT NULL AUTO_INCREMENT,
  `cname` varchar(255) NOT NULL,
  `tnumber` varbinary(255) DEFAULT NULL,
  PRIMARY KEY (`cid`) USING BTREE,
  KEY `cname` (`cname`) USING BTREE,
  KEY `cname_2` (`cname`,`cid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `classes`
--

LOCK TABLES `classes` WRITE;
/*!40000 ALTER TABLE `classes` DISABLE KEYS */;
INSERT INTO `classes` VALUES (1,'软件2108','100010'),(2,'英语2101','100001'),(3,'软件2106','444'),(4,'英语2102','100002'),(5,'软件2101','100001'),(6,'英语2103','100001'),(7,'东语2101','100001');
/*!40000 ALTER TABLE `classes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `download`
--

DROP TABLE IF EXISTS `download`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `download` (
  `did` int(11) NOT NULL AUTO_INCREMENT COMMENT '表的id（唯一）',
  `cid` int(11) DEFAULT NULL COMMENT '语料资源的id',
  `userid` varchar(255) DEFAULT NULL COMMENT '用户的id',
  `title` varchar(255) DEFAULT NULL COMMENT '语料标题',
  `urlfile` varchar(255) DEFAULT NULL COMMENT '文件地址',
  `who` int(11) DEFAULT NULL COMMENT '身份验证，1学生，2,教师，3管理员',
  PRIMARY KEY (`did`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `download`
--

LOCK TABLES `download` WRITE;
/*!40000 ALTER TABLE `download` DISABLE KEYS */;
INSERT INTO `download` VALUES (1,1,'20230001','Cancer-treatment-could-get-a-vaccine\r\nCancer-treatment-could-get-a-vaccine\r\nCancer-treatment-could-get-a-vaccine\r\n','/uploads/cancer_treatment_could_get_a_vaccine_b326cf9cd8.mp3',1),(2,11,'123','测试','dsdsd.mp3',1);
/*!40000 ALTER TABLE `download` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favorites`
--

DROP TABLE IF EXISTS `favorites`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `favorites` (
  `fid` int(11) NOT NULL AUTO_INCREMENT COMMENT '收藏id',
  `cid` int(11) DEFAULT NULL COMMENT '语料ID',
  `ctitle` varchar(255) DEFAULT NULL COMMENT '语料标题',
  `who` int(11) DEFAULT NULL COMMENT '收藏者的身份信息，1（学生）2（教师）3（管理员）',
  `userid` varchar(255) DEFAULT NULL COMMENT '收藏者id',
  `tagids` int(11) DEFAULT NULL COMMENT '语料的类型',
  PRIMARY KEY (`fid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favorites`
--

LOCK TABLES `favorites` WRITE;
/*!40000 ALTER TABLE `favorites` DISABLE KEYS */;
INSERT INTO `favorites` VALUES (11,6,'dkfv',12,'123345',33),(13,8,'dkfv',12,'123345',33),(17,9,'dkfv',12,'123345',33),(18,10,'dkfv',12,'123345',33),(19,5,'dkfv',12,'123345',33),(20,88,'dkfv',12,'123345',33),(28,10,'scdvwfbtgny',45356,'867345',33),(30,9,'scdvwfbtgny',45356,'867345',33),(54,2,'2011年3月上海市中级口译第二阶段口试真题口译题(4)',2,'100010',733),(60,2,'2011年3月上海市中级口译第二阶段口试真题口译题(4)',2,'100001',733),(62,2,'2011年3月上海市中级口译第二阶段口试真题口译题(4)',1,'10004',733),(63,4,'2011年3月上海市中级口译第二阶段口试真题口译题(*)',2,'100001',733),(64,9,'2010年3月上海市中级口译第二阶段口试真题口译题(3)',2,'100001',733),(66,0,'',2,'100001',733),(72,21,'中国5G技术',1,'210001',733),(73,5,'2010年9月上海市中级口译第二阶段口试真题口译题(3)',1,'2112190509',733);
/*!40000 ALTER TABLE `favorites` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mark`
--

DROP TABLE IF EXISTS `mark`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mark` (
  `mid` int(11) NOT NULL AUTO_INCREMENT COMMENT '自身唯一id\r\n\r\n\r\n',
  `eid` int(11) DEFAULT NULL COMMENT '练习表的唯一属性\r\n',
  `stunumber` varchar(255) DEFAULT NULL COMMENT '对应学生的唯一属性\r\n\r\n',
  `mtext` varchar(255) DEFAULT NULL COMMENT '评价内容\r\n',
  `mtime` float DEFAULT NULL COMMENT '时间戳',
  PRIMARY KEY (`mid`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mark`
--

LOCK TABLES `mark` WRITE;
/*!40000 ALTER TABLE `mark` DISABLE KEYS */;
INSERT INTO `mark` VALUES (1,1,'123','这段还不错',12.36),(2,1,'123','这段读的很不错',14.23),(3,1,'123','更新标记',16.52),(4,1,'123','线上测试评论标记',12.21),(6,1,'210001','这里发音不标准',10.21),(7,1,'210001','这里的翻译很不错，很准确',15.21),(8,1,'210001','这里的意思错了，请注重复习',30.43),(9,1,'123','七二五乳峰提供任何',12.21),(10,1,'123','',12.21),(11,1,'123','五七二发布通告',12.21),(12,1,'123','',12.21),(13,1,'123','此处发音不准确，注意',12.21),(14,1,'123','此处少译了个单词，注意',12.21),(15,1,'123','此处发音不准确',12.21),(16,1,'123','此处发音不准确',12.21),(17,1,'123','                                这个地方的socket单词口译有误。 ',12.21),(18,1,'123','',12.21),(19,1,'123','',12.21),(20,1,'123','还不错',12.21),(21,1,'210001','好好',1.37042),(22,1,'210001','很好',1.70661),(23,19,'210001','还行',2.04),(24,21,'210001','此处发音不标准',4.06),(25,1,'210001','个',2.05),(26,1,'210001','',2.04),(27,29,'210003','翻译不准',4.32);
/*!40000 ALTER TABLE `mark` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `students`
--

DROP TABLE IF EXISTS `students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `students` (
  `sid` int(11) NOT NULL AUTO_INCREMENT,
  `stunumber` varchar(255) NOT NULL COMMENT '学生学号（联系表中名为stuID）',
  `stuname` varchar(255) NOT NULL COMMENT '学生姓名-真实姓名',
  `stupassword` varchar(255) NOT NULL COMMENT '学生密码',
  `stuclass` varchar(255) NOT NULL COMMENT '学生班级',
  `stuphone` varchar(255) NOT NULL COMMENT '学生电话',
  `stusex` int(11) NOT NULL COMMENT '学生性别(1男，0女)',
  PRIMARY KEY (`sid`,`stunumber`) USING BTREE,
  UNIQUE KEY `stunumber` (`stunumber`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `students`
--

LOCK TABLES `students` WRITE;
/*!40000 ALTER TABLE `students` DISABLE KEYS */;
INSERT INTO `students` VALUES (1,'210001','同学A','$2a$10$R8QJvj0VIQDnAdpRLRXFEeYubLJplF5M2K6/AI/8dZORKecLMmjIS','英语2101','15987654321',0),(2,'210003','同学C','$2a$10$ZX3mEh8v0EGY6vSDzQkLF.PMRD1IYzSowF6YHwaqJxXRJnBqemDtm','英语2101','13065432198',1),(3,'210002','马大哈','$2a$10$N6796l4g5rPyO38khRCejeqnClk9rPJR1/yP23tMzzI7P51onRjue','2','11111111111',0),(4,'210004','同学B','$2a$10$sbkP9OenTD1z7j89JuDA2eYLqm5UbXC0vHUjg3j3fz63Ky8K/ZeXi','英语2101','12333333333',1),(5,'111000','123','$2a$10$Uzx/bojm6sZDbTmQUWX/1uUwFOtc7/7fMR0OuAx/0npygDRoRM7pa','软件2108','123',0),(6,'210005','同学D','$2a$10$1EtoAWwvEjlXZpwG5XIS0uDdjOY4D.PVa3ArhBiYHa9/7QmESga4K','软件2108','12345555555',0),(7,'2112190506','海绵宝宝','$2a$10$EO9QglmpcA8NKO6RraEfDOrDaZivFJHoWvBWG3GDRyCyWPNFVSAzi','东语2101','19357506805',0),(8,'2112190509','陈梨','$2a$10$9GXZTPxF1q0TtjT.EqqGyu0d1ISqUsYhtXPq9tXvmAlR8VAg28N1C','软件2101','15700163357',0);
/*!40000 ALTER TABLE `students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `task` (
  `tsid` int(11) NOT NULL AUTO_INCREMENT COMMENT '发布练习的主键id',
  `corpusid` int(11) DEFAULT NULL COMMENT '语料所对应的id',
  `classname` varchar(11) DEFAULT NULL COMMENT '发布的班级名称',
  `state` int(11) DEFAULT NULL COMMENT '发布的状态，1 发布、2 取消发布',
  `publishtime` datetime DEFAULT NULL COMMENT '发布时间',
  `taskname` varchar(255) DEFAULT NULL COMMENT '练习的名称（默认和语料的标题一致）',
  `teanumber` varchar(255) DEFAULT NULL COMMENT '教师的工号',
  PRIMARY KEY (`tsid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` VALUES (1,21,'英语2101',1,'2023-12-18 22:33:34','','100001'),(2,1,'英语2101',1,'2023-12-22 09:01:40','','100001'),(3,6,'英语2101',1,'2023-12-27 19:07:44','','100001'),(4,2,'软件2101',1,'2023-12-28 15:17:07','','100001'),(5,3,'英语2101',1,'2023-12-28 15:26:00','','100001');
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `teachers`
--

DROP TABLE IF EXISTS `teachers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `teachers` (
  `tid` int(11) NOT NULL AUTO_INCREMENT,
  `tnumber` varchar(255) NOT NULL COMMENT '教师的工号（语料表中AuthorID）',
  `teaname` varchar(255) NOT NULL COMMENT '教师姓名',
  `teapassword` varchar(255) NOT NULL COMMENT '教师密码',
  `tsex` int(11) NOT NULL COMMENT '教师性别（1男，0女）',
  `teastate` int(11) unsigned zerofill NOT NULL COMMENT '教师账号状态（1激活、0未激活-默认值、-1审核未通过）',
  `teaphone` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`tid`,`tnumber`) USING BTREE,
  KEY `tnumber` (`tnumber`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `teachers`
--

LOCK TABLES `teachers` WRITE;
/*!40000 ALTER TABLE `teachers` DISABLE KEYS */;
INSERT INTO `teachers` VALUES (7,'2023','刘老师','$2a$10$jRQ9dfPOC9O0cblUnlJKnujXyFaAz8AWlPaiNYoCUcfC5cUFvne2i',0,00000000001,'19875784562'),(8,'100001','王老师','$2a$10$LM8j9eAf5bXx.caPKliqx.KnXKYq2JNm4hoj4mXJp8O/87WKjnZ4G',0,00000000001,'15245685234'),(9,'100020','王西','$2a$10$p8wzrn5FvD.0dWrlSUVDm.5uGljIwRMv1cDVXv3wkOUjlHT7M6SFe',0,00000000001,''),(10,'200020','徐晓','$2a$10$ql6Q1pUUjnL3JROg8U4Z1.44XIm79tBIz6aPfSn6cuNrmXljyASm2',1,00000000001,'17857680542'),(11,'100010','杨老师','$2a$10$LM8j9eAf5bXx.caPKliqx.KnXKYq2JNm4hoj4mXJp8O/87WKjnZ4G',0,00000000001,'15952524601'),(18,'100008','徐老师','$2a$10$ZRZ.VdtA.Phuqgj2fk8R3uHwKaYLQIYeQWegdgDfbE1lfPwGmBRr.',0,00000000000,'12333333333');
/*!40000 ALTER TABLE `teachers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'corpusrole'
--

--
-- Dumping routines for database 'corpusrole'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-01 19:32:03
