package com.example.scheduledhome.component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PublisherComponent {

    @Value("${home.url}")
    private String url;

    @Value("${home.path}")
    private String path;

    private final RestTemplate restTemplate = new RestTemplate();

    public void publishDisable(String type, String deviceName) {
        String path = getPath(this.path, type);
        restTemplate.put(url + path , deviceName);
    }

    private String getPath(String path, String type) {
        return path.replace("%s", type);
    }
}
