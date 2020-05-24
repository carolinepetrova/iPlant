package com.project.iplant.mqtt_receiver.config;

import com.project.iplant.mqtt_receiver.subscriber.MQTTSubscriberBase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MessageListener implements Runnable{

	@Autowired
	MQTTSubscriberBase subscriber;
	
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			subscriber.subscribeMessage("soilmoisture");
		}
		
	}

}
