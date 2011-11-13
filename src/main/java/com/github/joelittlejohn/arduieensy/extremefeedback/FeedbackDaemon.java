package com.github.joelittlejohn.arduieensy.extremefeedback;

import java.net.MalformedURLException;
import java.net.URL;

public class FeedbackDaemon {

	private static final String ENV_VARIABLE_URL = "ARDUIEENSY_POLL_URL";

	public static void main(String[] args) {
		
		String url = System.getenv(ENV_VARIABLE_URL);
		
		try {
			Poller poller = new Poller(new Job(new URL(url)), new Orb());
			poller.start();
		} catch (MalformedURLException e) {
			System.err.println("Couldn't parse poll url, " + ENV_VARIABLE_URL + " had value: " + url);
		}
	}
	
}
