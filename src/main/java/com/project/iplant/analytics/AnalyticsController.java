package com.project.iplant.analytics;

import com.project.iplant.plantpictures.PlantPicturesRepository;
import com.project.iplant.plants_crud.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller
public class AnalyticsController {

    @Autowired
    private PlantRepository plantRepository;

    @Autowired
    private PlantPicturesRepository plantPicturesRepository;

    @GetMapping("analytics")
    public String listPlants(Model model) {
        model.addAttribute("plants", plantRepository.findAll());
        model.addAttribute("title", "Plant Analytics | iPlant");
        return "analytics";
    }

    @GetMapping("/analytics/health")
    public String showLastPictures(@Param(value="id") long id, Model model) {
        model.addAttribute("plantPictures", plantRepository.findAllById(new ArrayList<Long>() {{ add(id); }}));
        model.addAttribute("title", "Check Last Pictures of Plant | iPlant");
        return "health";
    }
}