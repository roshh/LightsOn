package com.github.roshh.light;

import java.io.IOException;

public class TrafficLight {

	private static final String CLEWARE_COMMAND = "sudo clewarecontrol -c 1 -d %s -as %s %s";
	private String deviceId;
	private boolean red;
	private boolean yellow;
	private boolean green;

	public TrafficLight(String deviceId) {
		this.deviceId = deviceId;
	}

	public synchronized void initialize() throws IOException, InterruptedException {
		System.out.println("initializing traffic light");
		toggleGreen(true);
		toggleYellow(true);
		toggleRed(true);

		toggleGreen(false);
		toggleYellow(false);
		toggleRed(false);
	}

	public synchronized void shutdown() throws IOException, InterruptedException {
		System.out.println("shutting down traffic light");

		toggleGreen(false);
		toggleYellow(false);
		toggleRed(false);
	}

	public synchronized void toggleRed(boolean on) throws IOException, InterruptedException {
		String stateCode = getStateCode(on);
		executeCommand("0", stateCode);
		this.red = on;
	}

	public synchronized void toggleYellow(boolean on) throws IOException, InterruptedException {
		String stateCode = getStateCode(on);
		executeCommand("1", stateCode);
		this.yellow = on;
	}

	public synchronized void toggleGreen(boolean on) throws IOException, InterruptedException {
		String stateCode = getStateCode(on);
		executeCommand("2", stateCode);
		this.green = on;
	}

	public synchronized void setRed(boolean animated) throws IOException, InterruptedException {
		if (this.green) {
			toggleGreen(false);
		}
		if (this.yellow) {
			toggleYellow(false);
		}
		if (!this.red) {
			toggleRed(true);
		} else if (animated) {
			toggleRed(false);
			toggleRed(true);
		}
	}

	public synchronized void setYellow(boolean animated) throws IOException, InterruptedException {
		if (this.green) {
			toggleGreen(false);
		}
		if (!this.yellow) {
			toggleYellow(true);
		} else if (animated) {
			toggleYellow(false);
			toggleYellow(true);
		}
		if (this.red) {
			toggleRed(false);
		}
	}

	public synchronized void setGreen(boolean animated) throws IOException, InterruptedException {
		if (!this.green) {
			toggleGreen(true);
		} else if (animated) {
			toggleGreen(false);
			toggleGreen(true);
		}
		if (this.yellow) {
			toggleYellow(false);
		}
		if (this.red) {
			toggleRed(false);
		}
	}

	private void executeCommand(String switchh, String state) throws IOException, InterruptedException {
		String command = String.format(CLEWARE_COMMAND, deviceId, switchh, state);

		Process process = Runtime.getRuntime().exec(command);
		process.waitFor();
	}

	private String getStateCode(boolean on) {
		if (on) {
			return "1";
		} else {
			return "0";
		}
	}

}
