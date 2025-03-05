package com.boa.tcautomation.controller;

import com.boa.tcautomation.model.TcExecutionLog;
import com.boa.tcautomation.service.TcExecutionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class TcExecutionLogController {

    @Autowired
    private TcExecutionLogService tcExecutionLogService;

    @GetMapping("/tc-execution-monitoring")
    public String getTcExecutionLogs(Model model) {
        List<TcExecutionLog> logs = tcExecutionLogService.getAllLogs();
        model.addAttribute("logs", logs);
        return "tc-execution-monitoring";
    }
}
