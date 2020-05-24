package com.project.iplant.robot;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.IOException;

@Controller
public class RobotConfigController {

    RobotConfig robotConfig;

    @GetMapping("/robot/robotconfig")
    public String showConfig(Model model) throws IOException {
        robotConfig = new RobotConfig("robot.properties");
        model.addAttribute("robot", robotConfig.getRobotProperties());
        model.addAttribute("title", "Change Robot Configuration | iPlant");
        return "robotconfig";
    }

    @PostMapping("/robot/robotconfig")
    public String addConfig(@ModelAttribute("robot") Robot robot, BindingResult result, Model model) throws IOException {
        if(robot.getAddress() == null)
            result.rejectValue("robotAddress", "Must.contain.value", "Field must not be empty");
        if (result.hasErrors()) {
            return "robotconfig";
        }
        robotConfig.setRobotProperties(robot);
        model.addAttribute("success", "Configuration was added successfully");
        return "redirect:/robot/robotconfig";
    }
}
