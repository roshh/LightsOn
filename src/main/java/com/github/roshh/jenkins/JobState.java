package com.github.roshh.jenkins;

public class JobState {
	
	private String name;
	private BuildColor color;
	private boolean animated;
	
	public JobState(String name, BuildColor color, boolean animated) {
		this.name = name;
		this.color = color;
		this.animated = animated;
	}

	public String getName() {
		return name;
	}

	public BuildColor getColor() {
		return color;
	}
	
	public boolean isAnimated() {
		return animated;
	}

	@Override
	public String toString() {
		return "JobState [name=" + name + ", color=" + color + ", animated=" + animated + "]";
	}
	
}
