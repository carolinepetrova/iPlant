package com.project.iplant.plants_crud;


import com.google.zxing.WriterException;
import com.project.iplant.plants_crud.Plant;
import com.project.iplant.plants_crud.PlantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

@Controller
public class PlantController {

    @Autowired
    private PlantRepository plantRepository;

    @GetMapping("/plants/addplant")
    public String listPlants(Model model) {
        model.addAttribute("plant", new Plant());
        model.addAttribute("title", "Add a New Plant | iPlant");
        return "addplant";
    }

    @GetMapping("/plants")
    public String addPlant(Model model) {
        model.addAttribute("plants", plantRepository.findAll());
        model.addAttribute("title", "All Plants | iPlant");
        return "plants";
    }

    @PostMapping("/plants/addplant")
    public String addPlant(@ModelAttribute("plant") Plant plantForm, BindingResult result, Model model) {
        if(plantForm.getWateringMilliliters() == 0)
            result.rejectValue("wateringMilliliters", "Must.contain.value", "Field is mandatory");
        if(plantForm.getWateringCycle() == 0)
            result.rejectValue("wateringCycle", "Must.contain.value", "Field is mandatory");

        if (result.hasErrors()) {
            System.out.println(result);
            return "addplant";
        }
        plantRepository.save(plantForm);
        model.addAttribute("plants", plantRepository.findAll());
        model.addAttribute("success", "Plant was added successfully");
        return "redirect:/plants";
    }

    @GetMapping("/plants/download")
    @ResponseBody
    public ResponseEntity<byte[]> generateAndDownloadQR(@Param(value="id") long id) {
        Plant plant = plantRepository.findById(id).orElse(null);
        if (plant == null) {
           return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(plant.QRCodeGenerator(), "jpg", outputStream);
        } catch (WriterException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        byte[] bytes = outputStream.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "image/jpeg");
        headers.set("Content-Disposition", "attachment; filename=\"" + plant.getPlant_id() + "_QR_"
                + "_" + plant.getType() + ".jpg\"");
        return new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.OK);
        }
}
