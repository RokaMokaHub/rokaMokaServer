package br.edu.ufpel.rokamoka.email;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.Map;

/**
 * Service for processing Thymeleaf templates
 */
@Service
@RequiredArgsConstructor
public class TemplateService {

    private final SpringTemplateEngine emailTemplateEngine;

    /**
     * Process a template with the given variables
     *
     * @param template the name of the template (without extension)
     * @param variables the variables to be used in the template
     *
     * @return the processed template as a string
     */
    public String processTemplate(String template, Map<String, Object> variables) {
        Context context = new Context();
        if (variables != null) {
            variables.forEach(context::setVariable);
        }
        return this.emailTemplateEngine.process(template, context);
    }
}
