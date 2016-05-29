package com.github.roshh.jenkins;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class JenkinsServer {

	private final String hostName;
	private DocumentBuilder documentBuilder = null;
	private final int port;

	public JenkinsServer(String hostName, int port) {
		this.hostName = hostName;
		this.port = port;
	}

	public synchronized JenkinsState getState() throws AccessException {
		Document document;
		try {
			String uri = "http://" + hostName + ":" + port + "/api/xml?xpath=/hudson/job&wrapper=state";
			document = getDocumentBuilder().parse(uri);
		} catch (SAXException  e) {
			throw new AccessException("XML Document can not be parsed", e);
		} catch (IOException e) {
			throw new AccessException("Server " + hostName + " not reachable", e);
		}

		return getBuildState(document);
	}

	JenkinsState getBuildState(Document document) {
		NodeList jobs = document.getDocumentElement().getChildNodes();
		
		Collection<JobState> jobStates = new ArrayDeque<JobState>();

		for (int i = 0; i < jobs.getLength(); i++) {
			Node item = jobs.item(i);
			if (item.getNodeType() != Element.ELEMENT_NODE) {
				continue;
			}
			
			Element jobElement = (Element) item;
			
			Element jobNameElement = (Element) jobElement.getElementsByTagName("name").item(0);
			Element colorWithAnimationElement = (Element) jobElement.getElementsByTagName("color").item(0);
			
			String nodeValue = colorWithAnimationElement.getTextContent();
			boolean animated = nodeValue.endsWith("_anime");

			String colorString = nodeValue.split("_")[0];
			BuildColor color = toColor(colorString);
			String jobName = jobNameElement.getTextContent();
			
			JobState jobState = new JobState(jobName, color, animated);
			jobStates.add(jobState);
		}

		JenkinsState buildState = new JenkinsState(jobStates);
		return buildState;
	}

	private BuildColor toColor(String colorString) {
		return BuildColor.valueOf(colorString.toUpperCase());
	}

	private DocumentBuilder getDocumentBuilder() {
		if (documentBuilder == null) {
			try {
				documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				throw new RuntimeException("Can not create new Document builder", e);
			}
		}

		return documentBuilder;
	}

}
