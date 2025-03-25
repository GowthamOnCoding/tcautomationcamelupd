package com.boa.tcautomation.controller;

import com.boa.tcautomation.model.TcMaster;
import com.boa.tcautomation.service.TcIdGeneratorService;
import com.boa.tcautomation.service.TcMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class TcMasterController {

    @Autowired
    private TcMasterService tcMasterService;
    @Autowired
    private TcIdGeneratorService tcIdGeneratorService;

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
       TcMaster tcMaster = new TcMaster();
       tcMaster.setTcId(tcIdGeneratorService.generateNextTcId());
        tcMaster.setCreatedBy("system");
        tcMaster.setCreatedDate(LocalDateTime.now());
        tcMaster.setModifiedBy("system");
        tcMaster.setModifiedDate(LocalDateTime.now());
        tcMaster.setIsActive("1");
        model.addAttribute("tcMaster", tcMaster);
        return "tcmasters/add";
    }

   @PostMapping("/tcmasters/save")
    public String saveTcMaster( @ModelAttribute("tcMaster") TcMaster tcMaster,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "tcmasters/add";
        }

        // For new records
        if (tcMaster.getCreatedDate() == null) {
            tcMaster.setCreatedDate(LocalDateTime.now());
        }
        if (tcMaster.getCreatedBy() == null) {
            tcMaster.setCreatedBy("system");
        }

        // Always update modified info
        tcMaster.setModifiedDate(LocalDateTime.now());
        tcMaster.setModifiedBy("system");

        tcMasterService.save(tcMaster);
        return "redirect:/tcmasters";
    }

    @GetMapping("/tcmasters/delete/{id}")
    public String deleteTcMaster(@PathVariable("id") String id) {
        tcMasterService.deleteById(id);
        return "redirect:/tcmasters";
    }
}
