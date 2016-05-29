package com.github.roshh.speech;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Iterator;

import com.github.roshh.jenkins.AccessException;
import com.github.roshh.jenkins.BuildColor;
import com.github.roshh.jenkins.JenkinsState;
import com.github.roshh.jenkins.JobState;

public class Speech {

	private final static String ESPEAK_COMMAND = "espeak -v%s+f3 -k5 -s150 -a200 --stdin";

	private Runtime runtime;

	public Speech() {
		runtime = Runtime.getRuntime();
	}

	private void speak(String text, String language) throws InterruptedException, IOException {
		String command = String.format(ESPEAK_COMMAND, language);
		Process process = runtime.exec(command);
		process.getOutputStream().write(text.getBytes(StandardCharsets.UTF_8));
		process.getOutputStream().close();
		process.waitFor();
	}

	public void initialize() throws IOException, InterruptedException {
		speak("Ab geht's!", "de");
	}

	public void shutdown() throws InterruptedException, IOException {
		speak("Machts gut.", "de");
	}

	public void vocalizeBuildStatus(JenkinsState jenkinsState) throws InterruptedException, IOException {
		Collection<JobState> redJobs = new ArrayDeque<>();
		Collection<JobState> yellowJobs = new ArrayDeque<>();

		for (JobState jobState : jenkinsState.getJobStates()) {
			BuildColor color = jobState.getColor();
			if (color == BuildColor.RED) {
				redJobs.add(jobState);
			} else if (color == BuildColor.YELLOW) {
				yellowJobs.add(jobState);
			}
		}

		int redJobsCount = redJobs.size();
		if (redJobsCount == 1) {
			speak("The job " + redJobs.iterator().next().getName() + " failed.", "en");
		} else if (redJobsCount > 1) {

			String text = "The jobs ";
			for (Iterator<JobState> iterator = redJobs.iterator(); iterator.hasNext();) {
				JobState job = iterator.next();

				if (iterator.hasNext()) {
					text += ", " + job.getName();
				} else {
					text += "and " + job.getName();
				}
			}

			text += " have failed.";

			speak(text, "en");
		}

		int yellowJobsCount = yellowJobs.size();
		if (yellowJobsCount == 1) {
			speak("The job " + yellowJobs.iterator().next().getName() + " is unstable.", "en");
		} else if (yellowJobsCount > 1) {

			String text = "The jobs ";
			for (Iterator<JobState> iterator = yellowJobs.iterator(); iterator.hasNext();) {
				JobState job = iterator.next();

				if (iterator.hasNext()) {
					text += ", " + job.getName();
				} else {
					text += "and " + job.getName();
				}
			}

			text += " are unstable.";

			speak(text, "en");
		}

		if (yellowJobs.isEmpty() && redJobs.isEmpty()) {
			speak("We are back to stable.", "en");
		}
	}

	public void callForHelp(AccessException e) throws InterruptedException, IOException {
		String message = createHelpMessage(e);

		speak(message, "en");
	}

	protected String createHelpMessage(AccessException e) {
		StringBuilder message = new StringBuilder();

		Throwable cause = e;
		do {
			if (message.length() == 0) {

				message.append("Help! " + cause.getMessage() + ";");
			} else {
				message.append(" because " + cause.getMessage() + ";");
			}
		} while ((cause = cause.getCause()) != null);
		return message.toString();
	}

}
