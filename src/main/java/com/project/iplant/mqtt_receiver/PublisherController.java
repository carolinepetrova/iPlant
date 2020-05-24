package com.project.iplant.mqtt_receiver;

import com.google.gson.JsonObject;
import com.project.iplant.mqtt_receiver.publisher.MQTTPublisherBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublisherController {
    @Autowired
    MQTTPublisherBase publisher;

    @RequestMapping(value = "api/mqtt/", method = RequestMethod.POST)
    public String index(@RequestBody String data) {
        publisher.publishMessage("soilmoisture2", data.toString());
        return "Message sent to Broker";
    }
}
