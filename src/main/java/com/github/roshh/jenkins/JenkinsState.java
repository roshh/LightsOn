package com.github.roshh.jenkins;

import java.util.Collection;

public class JenkinsState {

	private Collection<JobState> jobStates;

	public JenkinsState(Collection<JobState> jobStates) {
		this.jobStates = jobStates;
	}

	public BuildColor getWorstColor() {
		BuildColor worstColor = BuildColor.BLUE;

		for (JobState jobState : jobStates) {
			BuildColor color = jobState.getColor();
			if (BuildColor.RED == color) {
				worstColor = color;
			} else if (color == BuildColor.YELLOW && worstColor != BuildColor.RED) {
				worstColor = color;
			}
		}

		return worstColor;
	}
	
	public Collection<JobState> getJobStates() {
		return jobStates;
	}

	public boolean isAnimated() {
		for (JobState jobState : jobStates) {
			if (jobState.isAnimated()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean equals(JenkinsState oldState) {
		if (oldState == null) {
			return false;
		}
		
		return getWorstColor() == oldState.getWorstColor();
	}

}
