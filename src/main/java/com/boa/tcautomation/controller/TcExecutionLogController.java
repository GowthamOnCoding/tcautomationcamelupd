package com.boa.tcautomation.controller;

import com.boa.tcautomation.model.TcExecutionLog;
import com.boa.tcautomation.service.TcExecutionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class TcExecutionLogController {

    @Autowired
    private TcExecutionLogService tcExecutionLogService;

    @GetMapping("/tc-execution-monitoring")
    public String getTcExecutionLogs(Model model) {
        List<TcExecutionLog> logs = tcExecutionLogService.getAllLogs();
       Map<String, List<TcExecutionLog>> groupedLogs = logs.stream()
                .collect(Collectors.groupingBy(TcExecutionLog::getTcId));

        Map<String, String> tcIdStatuses = groupedLogs.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            List<TcExecutionLog> logList = entry.getValue();
                            if (logList.stream().allMatch(log -> log.getStatus().equals("Completed"))) {
                                return "Completed";
                            } else if (logList.stream().anyMatch(log -> log.getStatus().equals("Failed"))) {
                                return "Failed";
                            } else if (logList.stream().anyMatch(log -> log.getStatus().equals("In Progress"))) {
                                return "In Progress";
                            } else {
                                return "Pending";
                            }
                        }
                ));

        model.addAttribute("groupedLogs", groupedLogs);
        model.addAttribute("tcIdStatuses", tcIdStatuses);
        return "tc-execution-monitoring";
    }
}
