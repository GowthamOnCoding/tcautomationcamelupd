package com.boa.tcautomation.controller;

import com.boa.tcautomation.model.TcSteps;
import com.boa.tcautomation.service.TcStepsService;
import com.boa.tcautomation.service.TestAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class TcStepsController {

    @Autowired
    private TcStepsService tcStepsService;
    @Autowired
    private TestAPIService testManagementService;

    // Use an absolute path or a path relative to the project root
   @Value("${file.upload.sql-dir}")
    private String uploadRootDir;

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
        model.addAttribute("testCases", testManagementService.getAllTestCases());
        model.addAttribute("stepConfigs", testManagementService.getAllStepConfigs());
        return "tcsteps/add";
    }

    @PostMapping("/tcsteps/save")
    public String saveTcSteps(@ModelAttribute TcSteps tcSteps,
                              @RequestParam(value = "sqlFile", required = false) MultipartFile sqlFile) {

        String generatedFileName = null; // Variable to store the generated file name

        // Rename and store the SQL file if present
        if (sqlFile != null && !sqlFile.isEmpty() && "cleanAndInsertTestData".equals(tcSteps.getStepName())) {
            try {
                String originalFilename = StringUtils.cleanPath(sqlFile.getOriginalFilename());
                String extension = originalFilename.substring(originalFilename.lastIndexOf('.')); // .sql
                String sanitizedTcName = tcSteps.getTcId().replaceAll("[^a-zA-Z0-9]", "_");
                String stepName = tcSteps.getStepName();

                // Create the test case folder dynamically if it doesn't exist
                File testCaseFolder = new File(uploadRootDir, sanitizedTcName);
                if (!testCaseFolder.exists()) {
                    testCaseFolder.mkdirs();  // Create the test case folder if it doesn't exist
                }

                // Create the full path for the file
                generatedFileName = sanitizedTcName + "_" + stepName + "_" + tcSteps.getSequenceNo() + extension;
                File dest = new File(testCaseFolder, generatedFileName);

                // Ensure that the file path exists
                if (!dest.getParentFile().exists()) {
                    dest.getParentFile().mkdirs(); // Create any missing directories
                }

                // Transfer the file to the destination folder
                sqlFile.transferTo(dest);

                // Append the generated file name to the parameters field
                String existingParameters = tcSteps.getParameters();
                if (existingParameters == null || existingParameters.isEmpty()) {
                    tcSteps.setParameters(dest.getAbsolutePath()); // Set the generated file name if parameters is empty
                } else {
                    if(existingParameters.equalsIgnoreCase("{}")){
                        tcSteps.setParameters(dest.getAbsolutePath()); // Append the file name to existing parameters
                    } else {
                        tcSteps.setParameters(existingParameters + "," + dest.getAbsolutePath()); // Append the file name to existing parameters
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/tcsteps/add?error=fileUploadFailed";
            }
        }

        tcStepsService.save(tcSteps); // Save tcSteps, now with the file name in parameters
        return "redirect:/tcsteps";
    }

    @GetMapping("/tcsteps/delete/{id}")
    public String deleteTcSteps(@PathVariable("id") String id) {
        tcStepsService.deleteById(id);
        return "redirect:/tcsteps";
    }
}
