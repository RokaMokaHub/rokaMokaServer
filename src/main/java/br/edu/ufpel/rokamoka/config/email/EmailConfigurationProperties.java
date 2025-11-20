package br.edu.ufpel.rokamoka.config.email;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Carvalho
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class EmailConfigurationProperties {

    private int port;
    private String host;
    private String defaultEncoding;
    private String username;
    private String password;
    private String protocol;
    private Map<String, String> properties = new HashMap<>();

}
