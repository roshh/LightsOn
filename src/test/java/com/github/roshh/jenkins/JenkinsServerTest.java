package com.github.roshh.jenkins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.w3c.dom.Document;

import com.github.roshh.jenkins.BuildColor;
import com.github.roshh.jenkins.JenkinsServer;
import com.github.roshh.jenkins.JenkinsState;

public class JenkinsServerTest {

	@Test
	public void testGetBuildState() throws Exception {
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse("src/test/resources/jenkinsState.xml");
		JenkinsState buildState = new JenkinsServer(null, 1).getBuildState(document);
		
		assertTrue(buildState.isAnimated());
		assertEquals(BuildColor.YELLOW, buildState.getWorstColor());
	}

}
