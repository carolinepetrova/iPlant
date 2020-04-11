package com.project.iplant;

import com.project.iplant.app_endpoint.RobotMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.awt.*;

@Controller
public class HelloController {

    RobotMode robotMode = new RobotMode();

    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("title", "Hello World!");
        return "home";
    }
}
