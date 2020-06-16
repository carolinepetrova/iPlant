package com.project.iplant.analytics;

import com.project.iplant.plantpictures.PlantPicturesRepository;
import com.project.iplant.plants_crud.Plant;
import com.project.iplant.plants_crud.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;

@Controller
public class AnalyticsController {

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private PlantPicturesRepository plantPicturesRepository;

    @GetMapping("/analytics")
    public String listPlants(Model model) {
        model.addAttribute("plants", plantRepository.findAll());
        model.addAttribute("title", "Plant Analytics | iPlant");
        return "analytics_prev";
    }

    @GetMapping("/analytics/show/{id}")
    public String showLastPictures(@PathVariable Long id, Model model) {
        String name;
        if(id == 2)
            name = "Magnolia";
        else name = "Orchid";
        model.addAttribute("plantPictures", name);
        model.addAttribute("title", "Check Analytics of Plant | iPlant");
        return "analytics";
    }
    @GetMapping("/analytics/check-health/{id}")
    public String checkHealth(@PathVariable Long id, Model model) {
        String name;
        if(id == 2)
            name = "Magnolia";
        else name = "Orchid";
        model.addAttribute("plantPictures", name);
        model.addAttribute("title", "Check Last Pictures of Plant | iPlant");
        return "health";
    }
}