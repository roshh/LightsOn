package com.github.roshh.speech;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.roshh.jenkins.AccessException;
import com.github.roshh.speech.Speech;

public class SpeechTest {

	@Test
	public void testCreateHelpMessage() {
		Speech speech = new Speech();
		
		AccessException accessException = new AccessException("Alles mist", new RuntimeException("weil nichts geht", new RuntimeException("und man nichts machen kann")));
		String helpMessage = speech.createHelpMessage(accessException);
		
		assertEquals("Help! Alles mist; because weil nichts geht; because und man nichts machen kann;", helpMessage);
		
	}

}
