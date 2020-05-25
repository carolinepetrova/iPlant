package com.project.iplant;

import com.project.iplant.metrics.service.InfluxDBService;
import com.project.iplant.mqtt.publisher.MQTTPublisher;
import com.project.iplant.mqtt.subscriber.MQTTSubscriber;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.influxdb.dto.Point;
import org.influxdb.dto.QueryResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.influxdb.InfluxDBProperties;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@EnableConfigurationProperties(InfluxDBProperties.class)
public class InfluxDBTest {

    @Autowired
    InfluxDBService influxDBService;

    @Autowired
    MQTTSubscriber mqttSubscriber;

    @Test
    void testInfluxConnection() {
        assertEquals(true, influxDBService.isInfluxConnected());
    }

    @Test
    void testWriteToDB() {
        influxDBService.query("Create database unit_test");
        influxDBService.setDatabase("unit_test");
        Point p = Point.measurement("test").time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("testMeasure", 50).build();
        influxDBService.write(p);
        QueryResult query = influxDBService.query("drop database unit_test");
        assertEquals(false,query.hasError());

    }

    @Test
    void testMQTTPublisherWithInflux() throws Exception {
        influxDBService.query("Create database unit_test_mqtt");
        influxDBService.setDatabase("unit_test_mqtt");

        MqttMessage message = new MqttMessage();
        message.setPayload("{\"plantID\": 3, \"moisture\": 50}".getBytes());
        mqttSubscriber.messageArrived("soilmoisture", message);

        QueryResult query = influxDBService.query("select * from soilmoisture");
        influxDBService.query("Drop database unit_test_mqtt");
    }
}
