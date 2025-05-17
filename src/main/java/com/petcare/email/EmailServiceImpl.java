package com.petcare.email;

import com.petcare.enums.EmailTemplate;
import com.petcare.utils.Constants;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j

public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final ResourceLoader resourceLoader;
    private final EmailTemplateEngine emailTemplate;

    @Override
    public void sendWelcomeEmail(String recipientEmail, String role, String displayName, String password) {
        String subject;
        EmailTemplate template;
        Map<String, Object> variables;

        if ("EMPLEADO".equalsIgnoreCase(role)) {
            subject = Constants.SUBJECT_WELCOME_EMPLOYEE;
            template = EmailTemplate.WELCOME_EMPLOYEE;
            variables = Map.of(
                "displayName", displayName.split("@")[0],
                "username", displayName,
                "password", password,
                "loginUrl", Constants.LOGIN_URL
            );
        } else if ("CLIENTE".equalsIgnoreCase(role)) {
            subject = Constants.SUBJECT_WELCOME_CLIENT;
            template = EmailTemplate.WELCOME_CLIENT;
            variables = Map.of("displayName", displayName);
        } else {
            throw new IllegalArgumentException("Rol no soportado: " + role);
        }

        try {
            String content = emailTemplate.render(template, variables);
            sendHtmlEmail(recipientEmail, subject, content);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el email de bienvenida", e);
        }
    }

    @Override
    public void sendPasswordRecoveryEmail(String recipientEmail, String recoveryLink) {
        try {
            log.debug("Enviando email de recuperación a: {} | Token: {}", recipientEmail, recoveryLink);

            Map<String, Object> variables = Map.of("recoveryLink", recoveryLink);
            String content = emailTemplate.render(EmailTemplate.PASSWORD_RECOVERY, variables);
            sendHtmlEmail(recipientEmail, Constants.SUBJECT_PASSWORD_RECOVERY, content);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el email de recuperación de contraseña", e);
        }
    }

    @Override
    public void sendAccountDeactivationEmail(String recipientEmail, String displayName, String token) {
        try {
            Map<String, Object> variables = Map.of(
                "displayName", displayName,
                "reactivationLink", Constants.buildReactivationLink(token)
            );
            String content = emailTemplate.render(EmailTemplate.ACCOUNT_DEACTIVATION, variables);
            sendHtmlEmail(recipientEmail, Constants.SUBJECT_ACCOUNT_DEACTIVATED, content);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el email de desactivación por inactividad", e);
        }
    }

    @Override
    public void sendAppointmentReminder(String recipientEmail, String name, String petName, String date, String time, String veterinarian, String location) {
        try {
            Map<String, Object> variables = Map.of(
                "name", name,
                "petName", petName,
                "date", date,
                "time", time,
                "location", location
            );
            String content = emailTemplate.render(EmailTemplate.APPOINTMENT_REMINDER, variables);
            sendHtmlEmail(recipientEmail, "Recordatorio de cita - PetCare", content);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el recordatorio de cita", e);
        }
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            String styledHtml = applyStyles(htmlBody);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            helper.setFrom(Constants.EMAIL_FROM_ADDRESS);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(styledHtml, true);
            helper.addInline("petcare-logo", new ClassPathResource("static/images/PetCare.png"));

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar email HTML", e);
        }
    }

    private String applyStyles(String htmlContent) {
        try {
            Resource resource = resourceLoader.getResource("classpath:templates/email/templates.css");
            byte[] fileData = Files.readAllBytes(resource.getFile().toPath());
            String commonStyles = new String(fileData, StandardCharsets.UTF_8);
            return htmlContent.replace("/* {{styles}} */", commonStyles);
        } catch (IOException e) {
            throw new RuntimeException("No se pudo cargar la hoja de estilos comunes", e);
        }
    }
}
