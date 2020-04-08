package com.project.iplant.app_endpoint;

import com.project.iplant.auth.model.User;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

@Controller
public class RobotModeController {

    @RequestMapping(value = "/robot-mode", method = RequestMethod.GET)
    public String robotHome(Model model) {
        return "robot-mode";
    }

    @PostMapping("/robot-mode")
    public String changeRobotMode(@ModelAttribute("robot") RobotMode robotMode) {
        String destUri = "ws://192.168.1.239:81";

        WebSocketClient client = new WebSocketClient();
        RobotModeClient socket = new RobotModeClient();
        try {
            client.start();

            URI echoUri = new URI(destUri);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(socket, echoUri, request);
            System.out.printf("Connecting to : %s%n", echoUri);

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
    return "robot-mode";
    }
}
