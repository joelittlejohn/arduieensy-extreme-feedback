package com.github.joelittlejohn.arduieensy.extremefeedback;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import com.github.joelittlejohn.arduieensy.extremefeedback.exception.CommunicationFailureException;
import com.github.joelittlejohn.arduieensy.extremefeedback.exception.UnexpectedResponseException;

public class Job {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final URL statusUrl;

    public Job(URL url) {
        try {
            this.statusUrl = new URL(url, "lastBuild/api/json");
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Failed to construct a valid status url using the given job url: " + url);
        }
    }

    public JobState getState() {

        try (InputStream inputStream = statusUrl.openStream()) {

            JsonNode lastBuild = objectMapper.readTree(inputStream);

            if (lastBuild.has("result")) {
                return JobState.valueOf(lastBuild.get("result").getTextValue());
            } else {
                throw new UnexpectedResponseException("Received a response that could not be understood (no 'result' value was present):\n" + lastBuild);
            }

        } catch (IOException e) {
            throw new CommunicationFailureException("Unable to read from status URL: " + statusUrl, e);
        }

    }

}
