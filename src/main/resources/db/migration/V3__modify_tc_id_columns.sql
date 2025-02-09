-- Drop foreign key constraint
ALTER TABLE tc_execution_log DROP FOREIGN KEY tc_execution_log_ibfk_1;

-- Modify tc_id column in tc_master
ALTER TABLE tc_master MODIFY COLUMN tc_id VARCHAR(255) NOT NULL;

-- Modify tc_id column in tc_execution_log
ALTER TABLE tc_execution_log MODIFY COLUMN tc_id VARCHAR(255);

-- Recreate foreign key constraint
ALTER TABLE tc_execution_log ADD CONSTRAINT tc_execution_log_ibfk_1 FOREIGN KEY (tc_id) REFERENCES tc_master(tc_id);
