package com.wifosell.zeus.config.property;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("app")
@Getter
@Setter
public class AppProperties {
    private String migration;

    @Value("${app.service-go-sendo}")
    private String serviceGoSendo;

    public String getServiceGoSendo() {
        return serviceGoSendo;
    }
}
