package com.boa.tcautomation.controller;

import com.boa.tcautomation.model.TcMaster;
import com.boa.tcautomation.service.TcMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TcMasterController {

    @Autowired
    private TcMasterService tcMasterService;

    @GetMapping("/tcmasters")
    public String listTcMasters(Model model) {
        List<TcMaster> tcMasters = tcMasterService.findAll();
        model.addAttribute("tcmasters", tcMasters);
        return "tcmasters/list";
    }

    @GetMapping("/tcmasters/edit/{id}")
    public String editTcMaster(@PathVariable("id") String id, Model model) {
        TcMaster tcMaster = tcMasterService.findById(id);
        if (tcMaster == null) {
            return "error/404";
        }
        model.addAttribute("tcMaster", tcMaster);
        return "tcmasters/edit";
    }

    @GetMapping("/tcmasters/add")
    public String addTcMaster(Model model) {
        model.addAttribute("tcMaster", new TcMaster());
        return "tcmasters/edit";
    }

    @PostMapping("/tcmasters/save")
    public String saveTcMaster(@ModelAttribute TcMaster tcMaster) {
        tcMasterService.save(tcMaster);
        return "redirect:/tcmasters";
    }

    @GetMapping("/tcmasters/delete/{id}")
    public String deleteTcMaster(@PathVariable("id") String id) {
        tcMasterService.deleteById(id);
        return "redirect:/tcmasters";
    }
}
