package com.boa.tcautomation.repository;

import com.boa.tcautomation.model.TcExecutionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TcExecutionLogRepository extends JpaRepository<TcExecutionLog, Long> {
}
