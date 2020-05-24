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


import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Controller
public class SchedulerController {

    @Autowired
    PlantRepository plantRepository;

    private void sendPayload(String payload) throws IOException {
        //RobotConfig robotConfig = new RobotConfig("robot.properties");

        //String destUri = robotConfig.getRobotProperties().getAddress();

        String destUri = "ws://192.168.1.239:81";

        WebSocketClient client = new WebSocketClient();
        AppEndpointSocketClient socket = new AppEndpointSocketClient();

        try {
            client.start();

            URI echoUri = new URI(destUri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.setMaxIdleTimeout(Long.MAX_VALUE);
            Future<Session> conn;
            conn = client.connect(socket, echoUri, request);
            socket.sendMessage(payload);
            // wait for closed socket connection.
            socket.awaitClose(5, TimeUnit.SECONDS);
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        this.sendPayload(scheduler.toJsonObject());

    return "scheduler";
    }
}
