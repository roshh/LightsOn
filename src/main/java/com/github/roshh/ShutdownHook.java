package com.github.roshh;

import java.io.IOException;

import com.github.roshh.light.TrafficLight;
import com.github.roshh.speech.Speech;

public class ShutdownHook implements Runnable {

	private Speech speech;
	private TrafficLight trafficLight;
	
	public ShutdownHook(Speech speech, TrafficLight trafficLight) {
		this.speech = speech;
		this.trafficLight = trafficLight;
	}

	@Override
	public void run() {
		try {
			speech.shutdown();
			trafficLight.shutdown();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}

	}

}
