package com.project.iplant.scheduler_service;

import com.project.iplant.plants_crud.PlantRepository;
import com.project.iplant.robot.RobotConfig;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Controller
public class SchedulerController {

    @Autowired
    PlantRepository plantRepository;

    private final String requestUrl = "http://localhost:8082/api/mqtt/";

    private String sendPayload(String payload) throws IOException {
        try {
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "text/plain;charset=UTF-8");
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            writer.write(payload);
            writer.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer jsonString = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                jsonString.append(line);
            }
            br.close();
            connection.disconnect();
            return jsonString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @GetMapping("/schedule")
    public String getScheduler(Model model) {
        model.addAttribute("scheduler", new Scheduler());
        model.addAttribute("plants", plantRepository.findAll());
        model.addAttribute("title", "Schedule Plant Check | iPlant");
        return "scheduler";
    }

    @PostMapping("/schedule")
    public String changeRobotMode(@ModelAttribute("scheduler") Scheduler scheduler,
                                  BindingResult result, Model model) throws IOException {
        if(scheduler.getDate().equals(null))
            result.rejectValue("date", "Must.contain.value", "Field must not be empty");
        if (result.hasErrors()) {
            return "scheduler";
        }
        System.out.println(scheduler.toJsonObject());

        System.out.println(this.sendPayload(scheduler.toJsonObject()));

    return "scheduler";
    }
}
