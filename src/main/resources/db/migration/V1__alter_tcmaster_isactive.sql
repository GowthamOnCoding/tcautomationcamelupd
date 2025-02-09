-- First, update existing values to proper boolean values
UPDATE tc_master SET is_active = 
    CASE 
        WHEN LOWER(is_active) IN ('true', '1', 'yes', 'y') THEN '1'
        ELSE '0'
    END;

-- Then alter the column type
ALTER TABLE tc_master MODIFY COLUMN is_active BOOLEAN DEFAULT TRUE;
