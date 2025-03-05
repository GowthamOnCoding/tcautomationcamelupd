package com.boa.tcautomation.service;

import com.boa.tcautomation.model.TcExecutionLog;
import com.boa.tcautomation.repository.TcExecutionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TcExecutionLogService {

    @Autowired
    private TcExecutionLogRepository tcExecutionLogRepository;

    public List<TcExecutionLog> getAllLogs() {
        return tcExecutionLogRepository.findAll();
    }
}
