package br.edu.ufpel.rokamoka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.env.Environment;
import org.springframework.lang.NonNull;

import java.util.TimeZone;

@Slf4j
@SpringBootApplication
@ConfigurationPropertiesScan("br.edu.ufpel.rokamoka.config")
public class RokaMokaApplication implements ApplicationContextAware, ApplicationListener<ContextClosedEvent> {

    private static final String TIMEZONE = "America/Sao_Paulo";
    private static ApplicationContext ctx;

    public static void main(String[] args) {
        String property = "user.timezone";
        System.setProperty(property, TIMEZONE);
        String timeZone = System.getProperty(property);
        TimeZone.setDefault(TimeZone.getTimeZone(timeZone));

        ctx = SpringApplication.run(RokaMokaApplication.class, args);

        Environment env = ctx.getEnvironment();
        log.warn("""
                 
                 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                 \t\tApplication [{}] is running
                 Timezone: [{}]
                 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%""",
                env.getProperty("spring.application.name"), timeZone);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        ctx = applicationContext;
    }

    @Override
    public void onApplicationEvent(@NonNull ContextClosedEvent event) {
        log.warn("""
                 
                 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                 \t\t !!! STOPPING APPLICATION !!!!
                 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%""");
    }
}
