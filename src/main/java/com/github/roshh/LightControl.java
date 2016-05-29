package com.github.roshh;

import java.io.IOException;

import org.xml.sax.SAXException;

import com.github.roshh.jenkins.AccessException;
import com.github.roshh.jenkins.BuildColor;
import com.github.roshh.jenkins.JenkinsServer;
import com.github.roshh.jenkins.JenkinsState;
import com.github.roshh.light.TrafficLight;
import com.github.roshh.speech.Speech;

public class LightControl {

	public static void main(String[] args) throws IOException, InterruptedException, SAXException {
		if (args.length != 3) {
			System.err.println("Jenkikns traffic light - usage: java -jar LightsOn-1.0.0.jar <Jenkins hostname> <Jenkins port> <traffic light device id>");
			return;
		}
		
		String jenkinsHostName = args[0];
		Integer jenkinsPort = Integer.parseInt(args[1]);
		String trafficLightDeviceId = args[2];
		
		JenkinsServer jenkinsServer = new JenkinsServer(jenkinsHostName, jenkinsPort);
		final TrafficLight trafficLight = new TrafficLight(trafficLightDeviceId);
		final Speech speech = new Speech();
		speech.initialize();
		trafficLight.initialize();

		Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownHook(speech, trafficLight)));

		boolean running = true;
		JenkinsState lastState = null;
		while (running) {
			JenkinsState state;
			try {
				state = jenkinsServer.getState();
				
				BuildColor worstColor = state.getWorstColor();
				boolean animated = state.isAnimated();

				if (worstColor == BuildColor.BLUE) {
					trafficLight.setGreen(false);
				} else if (worstColor == BuildColor.YELLOW) {
					trafficLight.setYellow(false);
				} else if (worstColor == BuildColor.RED) {
					trafficLight.setRed(false);
				}
				
				if (lastState != null && !state.equals(lastState) && !animated) {
					speech.vocalizeBuildStatus(state);
				}
				lastState = state;

				Thread.sleep(1000L);
			} catch (AccessException e) {
				speech.callForHelp(e);
				trafficLight.toggleGreen(false);
				trafficLight.toggleYellow(false);
				trafficLight.toggleRed(false);
				Thread.sleep(60000L);
			}
		
		}
	}

}
