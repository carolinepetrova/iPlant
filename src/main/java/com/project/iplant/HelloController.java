package com.project.iplant;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("title", "Hello World!");
        return "home";
    }
}
