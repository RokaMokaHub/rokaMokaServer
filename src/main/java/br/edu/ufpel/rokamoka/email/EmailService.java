package br.edu.ufpel.rokamoka.email;

import br.edu.ufpel.rokamoka.config.email.EmailConfigurationProperties;
import br.edu.ufpel.rokamoka.core.User;
import br.edu.ufpel.rokamoka.service.user.IUserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author MauricioMucci
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateService templateService;
    private final EmailConfigurationProperties emailConfigurationProperties;

    private final IUserService userService;

    private Optional<MimeMessage> tryToComposeEmail(EmailDetails details) {
        try {
            return Optional.of(this.composeEmail(details));
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Erro ao construir email [SUBJECT: {}] - [TO: {}]", details.subject(), details.to(), e);
        }
        return Optional.empty();
    }

    private MimeMessage composeEmail(EmailDetails details) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setSubject(details.subject());
        helper.setFrom(details.from());
        helper.setTo(details.to());
        helper.setText(details.body(), true);
        return mimeMessage;
    }

    public void sendForgotPasswordEmail(String email) {
        User user = this.userService.getByEmail(email);
        String sender = this.emailConfigurationProperties.getUsername();
        String subject = "RokaMokaApp - Redefinir Senha";
        String template = "/password_reset";

        // TODO add link
        Map<String, Object> variables = Map.of("username", user.getNome(), "link", "teste");
        String body = this.templateService.processTemplate(template, variables);

        EmailDetails details = new EmailDetails(sender, email, subject, body);
        this.sendEmail(details);
    }

    private void sendEmail(EmailDetails details) {
        Optional<MimeMessage> maybeMessage = this.tryToComposeEmail(details);
        if (maybeMessage.isEmpty()) {
            return;
        }
        MimeMessage mimeMessage = maybeMessage.get();
        this.javaMailSender.send(mimeMessage);
    }
}
