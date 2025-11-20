package br.edu.ufpel.rokamoka.config.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * Configuration class responsible for setting up the {@code JavaMailSender} bean with the appropriate configuration
 * for sending emails
 *
 * @author Mauricio Carvalho
 * @see EmailConfigurationProperties
 * @see JavaMailSender
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(value = EmailConfigurationProperties.class)
public class EmailConfiguration {

    private final EmailConfigurationProperties emailConfigurationProperties;

    @Bean
    public JavaMailSender mailSender() {
        return this.buildJavaMailSender();
    }

    private JavaMailSenderImpl buildJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.emailConfigurationProperties.getHost());
        mailSender.setPort(this.emailConfigurationProperties.getPort());
        mailSender.setDefaultEncoding(this.emailConfigurationProperties.getDefaultEncoding());
        mailSender.setUsername(this.emailConfigurationProperties.getUsername());
        mailSender.setPassword(this.emailConfigurationProperties.getPassword());
        mailSender.setProtocol(this.emailConfigurationProperties.getProtocol());
        mailSender.setJavaMailProperties(this.buildJavaMailProperties());
        return mailSender;
    }

    private Properties buildJavaMailProperties() {
        Properties properties = new Properties();
        properties.putAll(this.emailConfigurationProperties.getProperties());
        return properties;
    }
}
