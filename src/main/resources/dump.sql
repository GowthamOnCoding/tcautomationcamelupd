-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: testdb
-- ------------------------------------------------------
-- Server version	8.0.40

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

--
-- Table structure for table `ait_dbprop`
--

DROP TABLE IF EXISTS `ait_dbprop`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ait_dbprop` (
  `ait_no` varchar(255) NOT NULL,
  `id` varchar(255) NOT NULL,
  `profile` varchar(255) DEFAULT NULL,
  `db_type` varchar(255) DEFAULT NULL,
  `machine_name` varchar(255) DEFAULT NULL,
  `db_name` varchar(255) DEFAULT NULL,
  `schema_name` varchar(255) DEFAULT NULL,
  `user_id` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `topic_name` varchar(255) DEFAULT NULL,
  `jdbc_url` varchar(255) DEFAULT NULL,
  `no_of_connection` int DEFAULT NULL,
  `email_id` varchar(255) DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT NULL,
  `last_updated_user` varchar(255) DEFAULT NULL,
  `sdd_active` tinyint(1) DEFAULT NULL,
  `aiml_is_active` tinyint(1) DEFAULT NULL,
  `table_list` varchar(255) DEFAULT NULL,
  `exec_status` varchar(255) DEFAULT NULL,
  `ait_cadence` varchar(255) DEFAULT NULL,
  `idw_sdd` tinyint(1) DEFAULT NULL,
  `idw_udd` tinyint(1) DEFAULT NULL,
  `iedps_sdd` tinyint(1) DEFAULT NULL,
  `report_topic_name` varchar(255) DEFAULT NULL,
  `funnel_udd` tinyint(1) DEFAULT NULL,
  `funnel_sdd` tinyint(1) DEFAULT NULL,
  `aiml_sdd` tinyint(1) DEFAULT NULL,
  `aiml_udd` tinyint(1) DEFAULT NULL,
  `funnel_destination` varchar(255) DEFAULT NULL,
  `funnel_discovery` tinyint(1) DEFAULT NULL,
  `aiml_discovery` tinyint(1) DEFAULT NULL,
  `idw_discovery` tinyint(1) DEFAULT NULL,
  `iedps_discovery` tinyint(1) DEFAULT NULL,
  `aiml_validation` tinyint(1) DEFAULT NULL,
  `fft_destination` varchar(255) DEFAULT NULL,
  `ait_num` int DEFAULT NULL,
  `jdbc_con_str` varchar(255) DEFAULT NULL,
  `lob` varchar(255) DEFAULT NULL,
  `environment` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ait_no`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ait_dbprop`
--

LOCK TABLES `ait_dbprop` WRITE;
/*!40000 ALTER TABLE `ait_dbprop` DISABLE KEYS */;
INSERT INTO `ait_dbprop` VALUES ('1000','ORA1','public','ORACLE','machineName1','dbName1','schemaName1','userId1','password1','topicName1','jdbcUrl1',1,'email1@example.com',1,'user1',1,1,'tableList1','execStatus1','aitCadence1',1,1,1,'reportTopicName1',1,1,1,1,'funnelDestination1',1,1,1,1,1,'fftDestination1',1,'jdbcConStr1','lob1','environment1'),('1000','SQL1','public','SQLSERVER','machineName10','dbName10','schemaName10','userId10','password10','topicName10','jdbcUrl10',10,'email10@example.com',0,'user10',0,0,'tableList10','execStatus10','aitCadence10',0,0,0,'reportTopicName10',0,0,0,0,'funnelDestination10',1,1,1,1,0,'fftDestination10',10,'jdbcConStr10','lob10','environment10'),('2','id2','profile2','dbType2','machineName2','dbName2','schemaName2','userId2','password2','topicName2','jdbcUrl2',2,'email2@example.com',0,'user2',0,0,'tableList2','execStatus2','aitCadence2',0,0,0,'reportTopicName2',0,0,0,0,'funnelDestination2',0,0,0,0,0,'fftDestination2',2,'jdbcConStr2','lob2','environment2'),('3','id3','profile3','dbType3','machineName3','dbName3','schemaName3','userId3','password3','topicName3','jdbcUrl3',3,'email3@example.com',1,'user3',1,1,'tableList3','execStatus3','aitCadence3',1,1,1,'reportTopicName3',1,1,1,1,'funnelDestination3',1,1,1,1,1,'fftDestination3',3,'jdbcConStr3','lob3','environment3'),('4','id4','profile4','dbType4','machineName4','dbName4','schemaName4','userId4','password4','topicName4','jdbcUrl4',4,'email4@example.com',0,'user4',0,0,'tableList4','execStatus4','aitCadence4',0,0,0,'reportTopicName4',0,0,0,0,'funnelDestination4',0,0,0,0,0,'fftDestination4',4,'jdbcConStr4','lob4','environment4'),('5','id5','profile5','dbType5','machineName5','dbName5','schemaName5','userId5','password5','topicName5','jdbcUrl5',5,'email5@example.com',1,'user5',1,1,'tableList5','execStatus5','aitCadence5',1,1,1,'reportTopicName5',1,1,1,1,'funnelDestination5',1,1,1,1,1,'fftDestination5',5,'jdbcConStr5','lob5','environment5'),('6','id6','profile6','dbType6','machineName6','dbName6','schemaName6','userId6','password6','topicName6','jdbcUrl6',6,'email6@example.com',0,'user6',0,0,'tableList6','execStatus6','aitCadence6',0,0,0,'reportTopicName6',0,0,0,0,'funnelDestination6',0,0,0,0,0,'fftDestination6',6,'jdbcConStr6','lob6','environment6'),('7','id7','profile7','dbType7','machineName7','dbName7','schemaName7','userId7','password7','topicName7','jdbcUrl7',7,'email7@example.com',1,'user7',1,1,'tableList7','execStatus7','aitCadence7',1,1,1,'reportTopicName7',1,1,1,1,'funnelDestination7',1,1,1,1,1,'fftDestination7',7,'jdbcConStr7','lob7','environment7'),('8','id8','profile8','dbType8','machineName8','dbName8','schemaName8','userId8','password8','topicName8','jdbcUrl8',8,'email8@example.com',0,'user8',0,0,'tableList8','execStatus8','aitCadence8',0,0,0,'reportTopicName8',0,0,0,0,'funnelDestination8',0,0,0,0,0,'fftDestination8',8,'jdbcConStr8','lob8','environment8'),('9','id9','profile9','dbType9','machineName9','dbName9','schemaName9','userId9','password9','topicName9','jdbcUrl9',9,'email9@example.com',1,'user9',1,1,'tableList9','execStatus9','aitCadence9',1,1,1,'reportTopicName9',1,1,1,1,'funnelDestination9',1,1,1,1,1,'fftDestination9',9,'jdbcConStr9','lob9','environment9');
/*!40000 ALTER TABLE `ait_dbprop` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ait_scan_window`
--

DROP TABLE IF EXISTS `ait_scan_window`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ait_scan_window` (
  `ait_no` varchar(255) NOT NULL,
  `db_type` varchar(255) DEFAULT NULL,
  `scan_day` varchar(255) DEFAULT NULL,
  `start_time_est` varchar(255) DEFAULT NULL,
  `end_time_est` varchar(255) DEFAULT NULL,
  `last_updated` datetime(6) DEFAULT NULL,
  `last_updated_user` varchar(255) DEFAULT NULL,
  `profile` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ait_scan_window`
--

LOCK TABLES `ait_scan_window` WRITE;
/*!40000 ALTER TABLE `ait_scan_window` DISABLE KEYS */;
INSERT INTO `ait_scan_window` VALUES ('AIT_1000','ORACLE','MON','00:00','23:59','2025-02-01 17:12:33.000000','system','public'),('AIT_1000','ORACLE','TUE','00:00','23:59','2025-02-01 17:12:33.000000','system','public'),('AIT_1000','ORACLE','WED','00:00','23:59','2025-02-01 17:12:33.000000','system','public'),('AIT_1000','ORACLE','THU','00:00','23:59','2025-02-01 17:12:33.000000','system','public'),('AIT_1000','ORACLE','FRI','00:00','23:59','2025-02-01 17:12:33.000000','system','public'),('AIT_1000','ORACLE','SAT','00:00','23:59','2025-02-01 17:12:33.000000','system','public'),('AIT_1000','ORACLE','SUN','00:00','23:59','2025-02-01 17:12:33.000000','system','public'),('AIT_1000','SQLSERVER','MON','00:00','23:59','2025-02-01 17:12:33.000000','system','public'),('AIT_1000','SQLSERVER','TUE','00:00','23:59','2025-02-01 17:12:33.000000','system','public'),('AIT_1000','SQLSERVER','WED','00:00','23:59','2025-02-01 17:12:33.000000','system','public'),('AIT_1000','SQLSERVER','THU','00:00','23:59','2025-02-01 17:12:33.000000','system','public'),('AIT_1000','SQLSERVER','FRI','00:00','23:59','2025-02-01 17:12:33.000000','system','public'),('AIT_1000','SQLSERVER','SAT','00:00','23:59','2025-02-01 17:12:33.000000','system','public'),('AIT_1000','SQLSERVER','SUN','00:00','23:59','2025-02-01 17:12:33.000000','system','public');
/*!40000 ALTER TABLE `ait_scan_window` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kafka_stat`
--

DROP TABLE IF EXISTS `kafka_stat`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kafka_stat` (
  `ait_no` varchar(255) NOT NULL,
  `start_time` datetime DEFAULT NULL,
  `event` varchar(255) DEFAULT NULL,
  `process_no` varchar(255) DEFAULT NULL,
  `table_name` varchar(255) DEFAULT NULL,
  `total_rows` bigint DEFAULT NULL,
  `total_fft_instances` bigint DEFAULT NULL,
  `total_messages` int DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `duration` double DEFAULT NULL,
  `seq_no` int DEFAULT NULL,
  `remarks` varchar(255) DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `topic_name` varchar(255) DEFAULT NULL,
  `group_id` varchar(255) DEFAULT NULL,
  `db_type` varchar(255) DEFAULT NULL,
  `schema_name` varchar(255) DEFAULT NULL,
  `machine_name` varchar(255) DEFAULT NULL,
  `profile` varchar(255) DEFAULT NULL,
  `config_id` varchar(255) DEFAULT NULL,
  `last_updated_user` varchar(255) DEFAULT NULL,
  `scheduler_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ait_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kafka_stat`
--

LOCK TABLES `kafka_stat` WRITE;
/*!40000 ALTER TABLE `kafka_stat` DISABLE KEYS */;
INSERT INTO `kafka_stat` VALUES ('AIT001','2023-10-01 10:00:00','ProcessStarted','PROC001','customer_table',1000,500,10,'2023-10-01 10:05:00',300.5,1,'Initial processing started','2023-10-01 10:05:00','COMPLETED','customer_topic','group1','MySQL','public','Machine1','DEV','CONFIG001','admin','SCHED001'),('AIT002','2023-10-02 12:00:00','DataValidation','PROC002','order_table',2000,1000,20,'2023-10-02 12:10:00',600,2,'Data validation completed','2023-10-02 12:10:00','IN_PROGRESS','order_topic','group2','PostgreSQL','sales','Machine2','TEST','CONFIG002','user1','SCHED002'),('AIT003','2023-10-03 15:00:00','DataTransformation','PROC003','product_table',1500,750,15,'2023-10-03 15:20:00',1200.75,3,'Data transformation in progress','2023-10-03 15:20:00','PENDING','product_topic','group3','Oracle','inventory','Machine3','PROD','CONFIG003','user2','SCHED003');
/*!40000 ALTER TABLE `kafka_stat` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `parameter_schema`
--

DROP TABLE IF EXISTS `parameter_schema`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `parameter_schema` (
  `schema_id` varchar(255) NOT NULL,
  `SCHEMA_VERSION` int NOT NULL,
  `SCHEMA_DEFINITION` text NOT NULL,
  `IS_ACTIVE` tinyint(1) DEFAULT '1',
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `CREATED_DATE` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `MODIFIED_DATE` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`schema_id`),
  UNIQUE KEY `SCHEMA_ID` (`schema_id`,`SCHEMA_VERSION`),
  KEY `idx_parameter_schema_active` (`IS_ACTIVE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `parameter_schema`
--

LOCK TABLES `parameter_schema` WRITE;
/*!40000 ALTER TABLE `parameter_schema` DISABLE KEYS */;
INSERT INTO `parameter_schema` VALUES ('deleteInsertScanWindow',1,'{\"type\":\"object\",\"properties\":{\"aitNumber\":{\"type\":\"string\",\"pattern\":\"^AIT_\\\\d+$\"},\"expectedOutput\":{\"type\":\"string\"}},\"required\":[\"aitNumber\",\"expectedOutput\"]}',1,'Scanwindow reset parameters3','2025-02-01 09:58:00','2025-02-01 09:58:00'),('exportTableToCsv',1,'{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"aitNumber\":{\"type\":\"string\"},\"configId\":{\"type\":\"string\"},\"tableName\":{\"type\":\"string\"},\"whereCondition\":{\"type\":\"string\"},\"expectedOutput\":{\"type\":\"string\"}},\"required\":[\"expectedOutput\",\"tableName\"]}',1,'CSV export parameters','2025-02-01 03:12:37',NULL),('resetAndEnableTools',1,'{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"aitNumber\":{\"type\":\"string\",\"pattern\":\"^\\\\d+$\"},\"configId\":{\"type\":\"string\",\"pattern\":\"^(ORA1|SQL1)(,(ORA1|SQL1))*$\"},\"enableTools\":{\"type\":\"string\",\"pattern\":\"^(idw|funnel|iedps|aiml)(,(idw|funnel|iedps|aiml))*$\"},\"expectedOutput\":{\"type\":\"string\"}},\"required\":[\"enableTools\",\"expectedOutput\"]}',1,'Reset and enable tools parameter','2025-01-31 05:14:29',NULL),('runLinuxCommand',1,'{\"$schema\":\"http://json-schema.org/draft-07/schema#\",\"type\":\"object\",\"properties\":{\"hostname\":{\"type\":\"string\"},\"process\":{\"type\":\"string\"},\"reqType\":{\"type\":\"string\"},\"command\":{\"type\":\"string\"},\"bgFlag\":{\"type\":\"string\",\"enum\":[\"true\",\"false\"]},\"expectedOutput\":{\"type\":\"string\"}},\"required\":[\"hostname\",\"process\",\"reqType\",\"bgFlag\",\"expectedOutput\"]}',1,'Linux Command invocation parameters','2025-01-31 02:57:22',NULL);
/*!40000 ALTER TABLE `parameter_schema` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `step_config`
--

DROP TABLE IF EXISTS `step_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `step_config` (
  `step_name` varchar(255) NOT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `TIMEOUT_SECONDS` int NOT NULL DEFAULT '300',
  `MAX_RETRIES` int NOT NULL DEFAULT '3',
  `PARAMETER_SCHEMA` varchar(100) DEFAULT NULL,
  `CREATED_DATE` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `MODIFIED_DATE` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`step_name`),
  KEY `PARAMETER_SCHEMA` (`PARAMETER_SCHEMA`),
  CONSTRAINT `step_config_ibfk_1` FOREIGN KEY (`PARAMETER_SCHEMA`) REFERENCES `parameter_schema` (`schema_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `step_config`
--

LOCK TABLES `step_config` WRITE;
/*!40000 ALTER TABLE `step_config` DISABLE KEYS */;
INSERT INTO `step_config` VALUES ('DELETE_INSERT_AIT_SCAN_WINDOW','Deletes and inserts AIT scan window records',300,3,NULL,'2025-01-29 13:41:48',NULL),('deleteInsertScanWindow','Deletes and inserts AIT scan window records',300,3,'deleteInsertScanWindow','2025-01-31 02:15:56',NULL),('exportTableToCsv','Export a table data to csv',300,3,'exportTableToCsv','2025-01-31 02:39:11',NULL),('INVOKE_JAVA_PROCESS','Invokes a Java process on a remote server',600,2,NULL,'2025-01-29 13:41:48',NULL),('resetAndEnableTools','Reset DBPROP for a given AIT and config Ids and enable the given tools',300,3,'resetAndEnableTools','2025-01-31 05:16:20',NULL),('runLinuxCommand','invoke a linux command on a remote server',300,3,'runLinuxCommand','2025-01-31 02:18:02',NULL);
/*!40000 ALTER TABLE `step_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `step_schema_mapping`
--

DROP TABLE IF EXISTS `step_schema_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `step_schema_mapping` (
  `step_name` varchar(255) NOT NULL,
  `schema_id` varchar(255) NOT NULL,
  `IS_REQUIRED` tinyint(1) DEFAULT '1',
  `SEQUENCE_NO` int NOT NULL,
  `CREATED_DATE` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`step_name`,`schema_id`),
  KEY `SCHEMA_ID` (`schema_id`),
  CONSTRAINT `step_schema_mapping_ibfk_1` FOREIGN KEY (`step_name`) REFERENCES `step_config` (`step_name`),
  CONSTRAINT `step_schema_mapping_ibfk_2` FOREIGN KEY (`schema_id`) REFERENCES `parameter_schema` (`schema_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `step_schema_mapping`
--

LOCK TABLES `step_schema_mapping` WRITE;
/*!40000 ALTER TABLE `step_schema_mapping` DISABLE KEYS */;
/*!40000 ALTER TABLE `step_schema_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tc_execution_log`
--

DROP TABLE IF EXISTS `tc_execution_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tc_execution_log` (
  `EXECUTION_ID` bigint NOT NULL AUTO_INCREMENT,
  `TC_ID` varchar(50) DEFAULT NULL,
  `STEP_ID` int DEFAULT NULL,
  `START_TIME` timestamp NULL DEFAULT NULL,
  `END_TIME` timestamp NULL DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `ERROR_MESSAGE` text,
  PRIMARY KEY (`EXECUTION_ID`),
  KEY `idx_execution_log_tcid` (`TC_ID`),
  CONSTRAINT `tc_execution_log_ibfk_1` FOREIGN KEY (`TC_ID`) REFERENCES `tc_master` (`TC_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tc_execution_log`
--

LOCK TABLES `tc_execution_log` WRITE;
/*!40000 ALTER TABLE `tc_execution_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `tc_execution_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tc_expected_output`
--

DROP TABLE IF EXISTS `tc_expected_output`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tc_expected_output` (
  `id` int NOT NULL,
  `ExpectedOutput` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tc_expected_output`
--

LOCK TABLES `tc_expected_output` WRITE;
/*!40000 ALTER TABLE `tc_expected_output` DISABLE KEYS */;
INSERT INTO `tc_expected_output` VALUES (1,'ScanWindow-Reset'),(2,'KafkaStat-Delete'),(3,'FileMovement'),(4,'CSVExport'),(5,'EnableTools'),(6,'LinuxCommandExecution');
/*!40000 ALTER TABLE `tc_expected_output` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tc_master`
--

DROP TABLE IF EXISTS `tc_master`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tc_master` (
  `TC_ID` varchar(50) NOT NULL,
  `tc_name` varchar(255) DEFAULT NULL,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `CREATED_DATE` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_by` varchar(255) DEFAULT NULL,
  `MODIFIED_DATE` timestamp NULL DEFAULT NULL,
  `AIT_NO` varchar(45) DEFAULT NULL,
  `is_active` varchar(255) DEFAULT NULL,
  `config_ids` varchar(255) NOT NULL,
  PRIMARY KEY (`TC_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tc_master`
--

LOCK TABLES `tc_master` WRITE;
/*!40000 ALTER TABLE `tc_master` DISABLE KEYS */;
INSERT INTO `tc_master` VALUES ('TC001','SDD-Day0-Metadata','Testcase to test Day 0 SDD MD invocation','','2025-02-01 08:32:00','Gowtham','2025-02-01 08:33:00','AIT_1000',NULL,'ORA1,ORA2'),('TC002','SDD-DAy0-Producer','Testcase to test Day 0 Producer invocation',NULL,'2025-01-30 15:50:14',NULL,NULL,'AIT_2000','1',''),('TC003','TC_003','Day-1-FFT','Gowtham','2025-02-01 08:39:00','Gowtham','2025-02-01 08:40:00','AIT_3000','1','SQL1');
/*!40000 ALTER TABLE `tc_master` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tc_steps`
--

DROP TABLE IF EXISTS `tc_steps`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tc_steps` (
  `step_id` bigint NOT NULL AUTO_INCREMENT,
  `TC_ID` varchar(50) NOT NULL,
  `step_name` varchar(255) DEFAULT NULL,
  `PARAMETERS` text,
  `SEQUENCE_NO` int NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`step_id`,`TC_ID`),
  KEY `STEP_NAME` (`step_name`),
  KEY `idx_tc_steps_tcid` (`TC_ID`),
  CONSTRAINT `tc_steps_ibfk_1` FOREIGN KEY (`TC_ID`) REFERENCES `tc_master` (`TC_ID`),
  CONSTRAINT `tc_steps_ibfk_2` FOREIGN KEY (`step_name`) REFERENCES `step_config` (`step_name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tc_steps`
--

LOCK TABLES `tc_steps` WRITE;
/*!40000 ALTER TABLE `tc_steps` DISABLE KEYS */;
INSERT INTO `tc_steps` VALUES (1,'TC001','deleteInsertScanWindow','{\"aitNumber\":\"AIT_1000\"}',1,NULL),(2,'TC001','runLinuxCommand','{\"hostname\":\"test\",\"process\":\"producer\",\"reqType\":\"sdd\",\"command\":\"ps -ef | grep java\",\"bgFlag\":\"true\"}',5,NULL),(3,'TC001','exportTableToCsv','{\"aitNumber\":\"AIT_1000\",\"configId\":\"ORA1,SQL1\",\"tableName\":\"KAFKA_STAT\",\"whereCondition\":\"\",\"expectedOutput\":\"CSVExport\"}',4,NULL),(4,'TC001','resetAndEnableTools','{\"aitNumber\":\"1000\",\"configId\":\"ORA1,SQL1\",\"enableTools\":\"idw,funnel,iedps,aiml\"}',3,NULL),(5,'TC001','exportTableToCsv','{\"aitNumber\":\"AIT_1000\",\"configId\":\"ORA1,SQL1\",\"tableName\":\"AIT_DBPROP\",\"whereCondition\":\"\",\"expectedOutput\":\"CSVExport\"}',2,NULL);
/*!40000 ALTER TABLE `tc_steps` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-02-03  9:30:54
