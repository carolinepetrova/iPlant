package com.project.iplant;

import com.project.iplant.plants_crud.PlantRepository;
import com.project.iplant.robot.Robot;
import com.project.iplant.robot.RobotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private PlantRepository plantRepository;

    private RobotConfig robotConfig;

    private Map<String,String> getRobotProperties() throws IOException {
        robotConfig = new RobotConfig("robot.properties");
        Robot robot = robotConfig.getRobotProperties();
        Map<String, String> robotProps = new HashMap<>();
        robotProps.put("mode", robot.getMode());
        robotProps.put("lastChecked", robot.getLastChecked());
        return robotProps;
    }

    @GetMapping({"/", "/home"})
    public String welcome(Model model) {
        model.addAttribute("numPlants", plantRepository.count());
        try {
            model.addAllAttributes(getRobotProperties());
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        model.addAttribute("title", "Dashboard | iPlant");
        return "home";
    }

    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("title", "Hello World!");
        return "home";
    }
}
