-- Drop foreign key constraints if they exist
SET @constraint_exists = (SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE table_schema = 'testdb'
    AND table_name = 'tc_steps'
    AND constraint_name = 'tc_steps_ibfk_1');

SET @exec_drop_tc_steps = IF(@constraint_exists > 0,
    'ALTER TABLE tc_steps DROP FOREIGN KEY tc_steps_ibfk_1',
    'SELECT 1');
PREPARE stmt FROM @exec_drop_tc_steps;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

SET @constraint_exists = (SELECT COUNT(*)
    FROM information_schema.table_constraints
    WHERE table_schema = 'testdb'
    AND table_name = 'tc_execution_log'
    AND constraint_name = 'tc_execution_log_ibfk_1');

SET @exec_drop_tc_exec = IF(@constraint_exists > 0,
    'ALTER TABLE tc_execution_log DROP FOREIGN KEY tc_execution_log_ibfk_1',
    'SELECT 1');
PREPARE stmt FROM @exec_drop_tc_exec;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Modify tc_id column in all related tables to ensure they match
ALTER TABLE tc_master MODIFY COLUMN tc_id VARCHAR(255);
ALTER TABLE tc_steps MODIFY COLUMN tc_id VARCHAR(255);
ALTER TABLE tc_execution_log MODIFY COLUMN tc_id VARCHAR(255);

-- Recreate the foreign key constraints
ALTER TABLE tc_steps ADD CONSTRAINT tc_steps_ibfk_1 
FOREIGN KEY (tc_id) REFERENCES tc_master(tc_id);
ALTER TABLE tc_execution_log ADD CONSTRAINT tc_execution_log_ibfk_1 
FOREIGN KEY (tc_id) REFERENCES tc_master(tc_id);
