package com.shyampatel.webapp.githubplayroom.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "github")
@PropertySource(value = "classpath:github-server-properties.yml", factory = YamlPropertySourceFactory.class)
public class GithubServerProperties {

    private String hmac_key;

    private String token;

    private String base_url;

}