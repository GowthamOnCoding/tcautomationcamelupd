package com.boa.tcautomation.controller;

import com.boa.tcautomation.model.TcExecutionLog;
import com.boa.tcautomation.service.TcExecutionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class TcExecutionLogController {
    @Autowired
    private TcExecutionLogService tcExecutionLogService;

    @GetMapping("/tc-execution-monitoring")
    public String getTcExecutionLogs(Model model) {
        List<TcExecutionLog> logs = tcExecutionLogService.getAllLogs();

        // Convert statuses to match UI expectations
        logs.forEach(log -> {
            switch(log.getStatus()) {
                case "Completed": log.setStatus("SUCCESS"); break;
                case "Failed": log.setStatus("FAILURE"); break;
                case "In Progress": log.setStatus("RUNNING"); break;
                default: log.setStatus("PENDING"); break;
            }

            if (log.getSequenceNumber() == null) {
                log.setSequenceNumber(log.getStepId());
            }
        });

        // Group logs by test case ID
        Map<String, List<TcExecutionLog>> groupedLogs = logs.stream()
                .collect(Collectors.groupingBy(TcExecutionLog::getTcId));

        // Calculate test case statuses
        Map<String, String> tcIdStatuses = groupedLogs.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> {
                            List<TcExecutionLog> logsList = entry.getValue();
                            if (logsList.stream().anyMatch(log -> "FAILURE".equals(log.getStatus()))) {
                                return "FAILURE";
                            } else if (logsList.stream().anyMatch(log -> "RUNNING".equals(log.getStatus()))) {
                                return "RUNNING";
                            } else if (logsList.stream().anyMatch(log -> "PENDING".equals(log.getStatus()))) {
                                return "PENDING";
                            } else {
                                return "SUCCESS";
                            }
                        }
                ));

        // Sort steps by sequence number for each test case
        Map<String, List<TcExecutionLog>> sortedSteps = groupedLogs.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream()
                                .sorted(Comparator.comparing(TcExecutionLog::getSequenceNumber))
                                .collect(Collectors.toList())
                ));

        // Calculate progress metrics
        Map<String, Integer> completedStepCounts = new HashMap<>();
        Map<String, Integer> totalStepCounts = new HashMap<>();
        Map<String, Integer> completedSteps = new HashMap<>();

        groupedLogs.forEach((tcId, tcLogs) -> {
            int total = tcLogs.size();
            int completed = (int) tcLogs.stream()
                    .filter(log -> "SUCCESS".equals(log.getStatus()) || "FAILURE".equals(log.getStatus()))
                    .count();

            totalStepCounts.put(tcId, total);
            completedStepCounts.put(tcId, completed);
            completedSteps.put(tcId, total > 0 ? (completed * 100) / total : 0);
        });

        // Add all to model
        model.addAttribute("groupedLogs", groupedLogs);
        model.addAttribute("tcIdStatuses", tcIdStatuses);
        model.addAttribute("sortedSteps", sortedSteps);
        model.addAttribute("completedStepCounts", completedStepCounts);
        model.addAttribute("totalStepCounts", totalStepCounts);
        model.addAttribute("completedSteps", completedSteps);

        return "tc-execution-monitoring";
    }

    @GetMapping("/api/tc-execution-logs")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getLogsJson() {
        List<TcExecutionLog> logs = tcExecutionLogService.getAllLogs();

        // Convert statuses to match UI expectations
        logs.forEach(log -> {
            switch(log.getStatus()) {
                case "Completed": log.setStatus("SUCCESS"); break;
                case "Failed": log.setStatus("FAILURE"); break;
                case "In Progress": log.setStatus("RUNNING"); break;
                default: log.setStatus("PENDING"); break;
            }

            if (log.getSequenceNumber() == null) {
                log.setSequenceNumber(log.getStepId());
            }
        });

        Map<String, Object> response = new HashMap<>();
        response.put("logs", logs);

        return ResponseEntity.ok(response);
    }
}
