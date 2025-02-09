package com.boa.tcautomation.controller;

import com.boa.tcautomation.model.TcSteps;
import com.boa.tcautomation.service.TcStepsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TcStepsController {

    @Autowired
    private TcStepsService tcStepsService;

    @GetMapping("/tcsteps")
    public String listTcSteps(Model model) {
        List<TcSteps> tcStepsList = tcStepsService.findAll();
        model.addAttribute("tcsteps", tcStepsList);
        return "tcsteps/list";
    }

    @GetMapping("/tcsteps/edit/{id}")
    public String editTcSteps(@PathVariable("id") String id, Model model) {
        TcSteps tcSteps = tcStepsService.findById(id);
        if (tcSteps == null) {
            return "error/404";
        }
        model.addAttribute("tcSteps", tcSteps);
        return "tcsteps/edit";
    }

    @GetMapping("/tcsteps/add")
    public String addTcSteps(Model model) {
        model.addAttribute("tcSteps", new TcSteps());
        return "tcsteps/edit";
    }

    @PostMapping("/tcsteps/save")
    public String saveTcSteps(@ModelAttribute TcSteps tcSteps) {
        tcStepsService.save(tcSteps);
        return "redirect:/tcsteps";
    }

    @GetMapping("/tcsteps/delete/{id}")
    public String deleteTcSteps(@PathVariable("id") String id) {
        tcStepsService.deleteById(id);
        return "redirect:/tcsteps";
    }
}
