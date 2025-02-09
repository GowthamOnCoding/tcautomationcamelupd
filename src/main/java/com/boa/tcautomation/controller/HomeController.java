package com.boa.tcautomation.controller;

import com.boa.tcautomation.model.TcMaster;
import com.boa.tcautomation.model.ParameterSchema;
import com.boa.tcautomation.service.TcMasterService;
import com.boa.tcautomation.service.ParameterSchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private TcMasterService tcMasterService;

    @Autowired
    private ParameterSchemaService parameterSchemaService;

    @GetMapping("/")
    public String home(Model model) {
        List<TcMaster> tcMasters = tcMasterService.findAll();
        List<ParameterSchema> parameters = parameterSchemaService.findAll();
        model.addAttribute("tcmasters", tcMasters);
       return "tcmasters/list";
    }
}
