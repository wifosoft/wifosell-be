package com.wifosell.zeus.config.property;


import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@ConfigurationProperties("app")
@Getter
@Setter
public class AppProperties {
    private String migration;

    @Value("${app.service-go-sendo}")
    private String serviceGoSendo;

    @Value("${app.lazada-app-id}")
    private String lazadaAppId;

    @Value("${app.lazada-app-secret}")
    private String lazadaAppSecret;
    public String getServiceGoSendo() {
        return serviceGoSendo;
    }

    @Bean
    public String getLazadaAppId() {
        return lazadaAppId;
    }

    @Bean
    public String getLazadaAppSecret() {
        return lazadaAppSecret;
    }

    @Bean
    public String getMigration() {
        return migration;
    }
}
