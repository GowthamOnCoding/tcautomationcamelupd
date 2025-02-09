-- SQL Server version

-- Table structure for table `parameter_schema`
IF OBJECT_ID('parameter_schema', 'U') IS NOT NULL DROP TABLE parameter_schema;
CREATE TABLE parameter_schema (
  schema_id NVARCHAR(255) NOT NULL,
  SCHEMA_VERSION INT NOT NULL,
  SCHEMA_DEFINITION NVARCHAR(MAX) NOT NULL,
  IS_ACTIVE BIT DEFAULT 1,
  DESCRIPTION NVARCHAR(255) DEFAULT NULL,
  CREATED_DATE DATETIME DEFAULT GETDATE(),
  MODIFIED_DATE DATETIME DEFAULT NULL,
  PRIMARY KEY (schema_id, SCHEMA_VERSION)
);

-- Dumping data for table `parameter_schema`
INSERT INTO parameter_schema VALUES
('deleteInsertScanWindow', 1, '{"type":"object","properties":{"aitNumber":{"type":"string","pattern":"^AIT_\\\\d+$"},"expectedOutput":{"type":"string"}},"required":["aitNumber","expectedOutput"]}', 1, 'Scanwindow reset parameters3', '2025-02-01 09:58:00', '2025-02-01 09:58:00'),
('exportTableToCsv', 1, '{"$schema":"http://json-schema.org/draft-07/schema#","type":"object","properties":{"aitNumber":{"type":"string"},"configId":{"type":"string"},"tableName":{"type":"string"},"whereCondition":{"type":"string"},"expectedOutput":{"type":"string"}},"required":["expectedOutput","tableName"]}', 1, 'CSV export parameters', '2025-02-01 03:12:37', NULL),
('resetAndEnableTools', 1, '{"$schema":"http://json-schema.org/draft-07/schema#","type":"object","properties":{"aitNumber":{"type":"string","pattern":"^\\\\d+$"},"configId":{"type":"string","pattern":"^(ORA1|SQL1)(,(ORA1|SQL1))*$"},"enableTools":{"type":"string","pattern":"^(idw|funnel|iedps|aiml)(,(idw|funnel|iedps|aiml))*$"},"expectedOutput":{"type":"string"}},"required":["enableTools","expectedOutput"]}', 1, 'Reset and enable tools parameter', '2025-01-31 05:14:29', NULL),
('runLinuxCommand', 1, '{"$schema":"http://json-schema.org/draft-07/schema#","type":"object","properties":{"hostname":{"type":"string"},"process":{"type":"string"},"reqType":{"type":"string"},"command":{"type":"string"},"bgFlag":{"type":"string","enum":["true","false"]},"expectedOutput":{"type":"string"}},"required":["hostname","process","reqType","bgFlag","expectedOutput"]}', 1, 'Linux Command invocation parameters', '2025-01-31 02:57:22', NULL);

-- Table structure for table `step_config`
IF OBJECT_ID('step_config', 'U') IS NOT NULL DROP TABLE step_config;
CREATE TABLE step_config (
  step_name NVARCHAR(255) NOT NULL,
  DESCRIPTION NVARCHAR(255) DEFAULT NULL,
  TIMEOUT_SECONDS INT NOT NULL DEFAULT 300,
  MAX_RETRIES INT NOT NULL DEFAULT 3,
  PARAMETER_SCHEMA NVARCHAR(100) DEFAULT NULL,
  CREATED_DATE DATETIME DEFAULT GETDATE(),
  MODIFIED_DATE DATETIME DEFAULT NULL,
  PRIMARY KEY (step_name),
  FOREIGN KEY (PARAMETER_SCHEMA) REFERENCES parameter_schema(schema_id)
);

-- Dumping data for table `step_config`
INSERT INTO step_config VALUES
('DELETE_INSERT_AIT_SCAN_WINDOW', 'Deletes and inserts AIT scan window records', 300, 3, NULL, '2025-01-29 13:41:48', NULL),
('deleteInsertScanWindow', 'Deletes and inserts AIT scan window records', 300, 3, 'deleteInsertScanWindow', '2025-01-31 02:15:56', NULL),
('exportTableToCsv', 'Export a table data to csv', 300, 3, 'exportTableToCsv', '2025-01-31 02:39:11', NULL),
('INVOKE_JAVA_PROCESS', 'Invokes a Java process on a remote server', 600, 2, NULL, '2025-01-29 13:41:48', NULL),
('resetAndEnableTools', 'Reset DBPROP for a given AIT and config Ids and enable the given tools', 300, 3, 'resetAndEnableTools', '2025-01-31 05:16:20', NULL),
('runLinuxCommand', 'invoke a linux command on a remote server', 300, 3, 'runLinuxCommand', '2025-01-31 02:18:02', NULL);

-- Table structure for table `step_schema_mapping`
IF OBJECT_ID('step_schema_mapping', 'U') IS NOT NULL DROP TABLE step_schema_mapping;
CREATE TABLE step_schema_mapping (
  step_name NVARCHAR(255) NOT NULL,
  schema_id NVARCHAR(255) NOT NULL,
  IS_REQUIRED BIT DEFAULT 1,
  SEQUENCE_NO INT NOT NULL,
  CREATED_DATE DATETIME DEFAULT GETDATE(),
  PRIMARY KEY (step_name, schema_id),
  FOREIGN KEY (step_name) REFERENCES step_config(step_name),
  FOREIGN KEY (schema_id) REFERENCES parameter_schema(schema_id)
);

-- Table structure for table `tc_execution_log`
IF OBJECT_ID('tc_execution_log', 'U') IS NOT NULL DROP TABLE tc_execution_log;
CREATE TABLE tc_execution_log (
  EXECUTION_ID BIGINT IDENTITY(1,1) NOT NULL,
  TC_ID NVARCHAR(50) DEFAULT NULL,
  STEP_ID INT DEFAULT NULL,
  START_TIME DATETIME DEFAULT NULL,
  END_TIME DATETIME DEFAULT NULL,
  status NVARCHAR(255) DEFAULT NULL,
  ERROR_MESSAGE NVARCHAR(MAX),
  PRIMARY KEY (EXECUTION_ID),
  FOREIGN KEY (TC_ID) REFERENCES tc_master(TC_ID)
);

-- Table structure for table `tc_expected_output`
IF OBJECT_ID('tc_expected_output', 'U') IS NOT NULL DROP TABLE tc_expected_output;
CREATE TABLE tc_expected_output (
  id INT NOT NULL,
  ExpectedOutput NVARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (id)
);

-- Dumping data for table `tc_expected_output`
INSERT INTO tc_expected_output VALUES
(1, 'ScanWindow-Reset'),
(2, 'KafkaStat-Delete'),
(3, 'FileMovement'),
(4, 'CSVExport'),
(5, 'EnableTools'),
(6, 'LinuxCommandExecution');

-- Table structure for table `tc_master`
IF OBJECT_ID('tc_master', 'U') IS NOT NULL DROP TABLE tc_master;
CREATE TABLE tc_master (
  TC_ID NVARCHAR(50) NOT NULL,
  tc_name NVARCHAR(255) DEFAULT NULL,
  DESCRIPTION NVARCHAR(255) DEFAULT NULL,
  created_by NVARCHAR(255) DEFAULT NULL,
  CREATED_DATE DATETIME DEFAULT GETDATE(),
  modified_by NVARCHAR(255) DEFAULT NULL,
  MODIFIED_DATE DATETIME DEFAULT NULL,
  AIT_NO NVARCHAR(45) DEFAULT NULL,
  is_active NVARCHAR(255) DEFAULT NULL,
  config_ids NVARCHAR(255) NOT NULL,
  PRIMARY KEY (TC_ID)
);

-- Dumping data for table `tc_master`
INSERT INTO tc_master VALUES
('TC001', 'SDD-Day0-Metadata', 'Testcase to test Day 0 SDD MD invocation', '', '2025-02-01 08:32:00', 'Gowtham', '2025-02-01 08:33:00', 'AIT_1000', NULL, 'ORA1,ORA2'),
('TC002', 'SDD-DAy0-Producer', 'Testcase to test Day 0 Producer invocation', NULL, '2025-01-30 15:50:14', NULL, NULL, 'AIT_2000', '1', ''),
('TC003', 'TC_003', 'Day-1-FFT', 'Gowtham', '2025-02-01 08:39:00', 'Gowtham', '2025-02-01 08:40:00', 'AIT_3000', '1', 'SQL1');

-- Table structure for table `tc_steps`
IF OBJECT_ID('tc_steps', 'U') IS NOT NULL DROP TABLE tc_steps;
CREATE TABLE tc_steps (
  step_id BIGINT IDENTITY(1,1) NOT NULL,
  TC_ID NVARCHAR(50) NOT NULL,
  step_name NVARCHAR(255) DEFAULT NULL,
  PARAMETERS NVARCHAR(MAX),
  SEQUENCE_NO INT NOT NULL,
  status NVARCHAR(255) DEFAULT NULL,
  PRIMARY KEY (step_id, TC_ID),
  FOREIGN KEY (TC_ID) REFERENCES tc_master(TC_ID),
  FOREIGN KEY (step_name) REFERENCES step_config(step_name)
);

-- Dumping data for table `tc_steps`
INSERT INTO tc_steps VALUES
(1, 'TC001', 'deleteInsertScanWindow', '{"aitNumber":"AIT_1000"}', 1, NULL),
(2, 'TC001', 'runLinuxCommand', '{"hostname":"test","process":"producer","reqType":"sdd","command":"ps -ef | grep java","bgFlag":"true"}', 5, NULL),
(3, 'TC001', 'exportTableToCsv', '{"aitNumber":"AIT_1000","configId":"ORA1,SQL1","tableName":"KAFKA_STAT","whereCondition":"","expectedOutput":"CSVExport"}', 4, NULL),
(4, 'TC001', 'resetAndEnableTools', '{"aitNumber":"1000","configId":"ORA1,SQL1","enableTools":"idw,funnel,iedps,aiml"}', 3, NULL),
(5, 'TC001', 'exportTableToCsv', '{"aitNumber":"AIT_1000","configId":"ORA1,SQL1","tableName":"AIT_DBPROP","whereCondition":"","expectedOutput":"CSVExport"}', 2, NULL);