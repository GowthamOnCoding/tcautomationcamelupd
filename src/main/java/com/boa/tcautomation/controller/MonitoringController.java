package com.boa.tcautomation.controller;

import com.boa.tcautomation.repository.HostDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/monitor")
public class MonitoringController {

    @Autowired
    private HostDetailsRepository hostDetailsRepository;

    @GetMapping
    public String showMonitoringDashboard(Model model) {
        model.addAttribute("hosts", hostDetailsRepository.findByIsActiveTrue());
        return "monitoring/dashboard";
    }
}
