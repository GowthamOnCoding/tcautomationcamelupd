package com.boa.tcautomation.controller;

import com.boa.tcautomation.model.ParameterSchema;
import com.boa.tcautomation.service.ParameterSchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ParameterSchemaController {

    @Autowired
    private ParameterSchemaService parameterSchemaService;

    @GetMapping("/parameters")
    public String listParameters(Model model) {
        List<ParameterSchema> parameters = parameterSchemaService.findAll();
        model.addAttribute("parameters", parameters);
        return "paramschema/list";
    }

    @GetMapping("/parameters/edit/{schemaId}")
    public String editParameterSchema(@PathVariable("schemaId") String schemaId, Model model) {
        ParameterSchema parameterSchema = parameterSchemaService.findById(schemaId);
        if (parameterSchema == null) {
            return "error/404";
        }
        model.addAttribute("parameterSchema", parameterSchema);
        return "paramschema/edit";
    }

    @PostMapping("/parameters/save")
    public String saveParameterSchema(@ModelAttribute ParameterSchema parameterSchema) {
        parameterSchemaService.save(parameterSchema);
        return "redirect:/parameters";
    }

    @GetMapping("/parameters/add")
    public String addParameterSchema(Model model) {
        model.addAttribute("parameterSchema", new ParameterSchema());
        return "paramschema/add";
    }

    @GetMapping("/parameters/delete/{schemaId}")
    public String deleteParameterSchema(@PathVariable("schemaId") String schemaId) {
        parameterSchemaService.deleteById(schemaId);
        return "redirect:/parameters";
    }
}
