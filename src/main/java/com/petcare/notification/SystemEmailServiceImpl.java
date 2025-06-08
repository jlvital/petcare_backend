package com.petcare.notification;

import com.petcare.enums.EmailTemplate;
import com.petcare.enums.Role;
import com.petcare.utils.constants.*;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SystemEmailServiceImpl implements SystemEmailService {

    private final JavaMailSender mailSender;
    private final ResourceLoader resourceLoader;
    private final TemplateRenderer emailTemplate;

    @Value("${mail.from.address}")
    private String emailFromAddress;

    @Override
    public void sendWelcomeEmail(String recipientEmail, String role, String displayName, String username, String password) {
        EmailTemplate template;
        Map<String, Object> variables;

        if (Role.EMPLEADO.name().equalsIgnoreCase(role)) {
            template = EmailTemplate.WELCOME_EMPLOYEE;
            variables = Map.of(
                "name", displayName,
                "username", username,
                "password", password,
                "loginUrl", UrlConstants.LOGIN_URL
            );
        } else if (Role.CLIENTE.name().equalsIgnoreCase(role)) {
            template = EmailTemplate.WELCOME_CLIENT;
            variables = Map.of("name", displayName);
        } else {
            throw new IllegalArgumentException(EmailConstants.ROLE_NOT_SUPPORTED_ERROR + role);
        }

        try {
            String content = emailTemplate.render(template, variables);
            sendHtmlEmail(recipientEmail, template.getSubject(), content);
        } catch (Exception e) {
            throw new RuntimeException(EmailConstants.SEND_WELCOME_ERROR, e);
        }
    }

    @Override
    public void sendWelcomeEmail(String recipientEmail, String role, String displayName, String ignoredPassword) {
        sendWelcomeEmail(recipientEmail, role, displayName, null, ignoredPassword);
    }

    @Override
    public void sendWelcomeEmail(String recipientEmail, String role, String displayName, String username, String password, String resetLink) {
        EmailTemplate template;
        Map<String, Object> variables;

        if ("EMPLEADO".equalsIgnoreCase(role)) {
            template = EmailTemplate.WELCOME_EMPLOYEE;
            variables = Map.of(
                "name", displayName,
                "username", username,
                "password", password,
                "resetLink", resetLink
            );
        } else if ("CLIENTE".equalsIgnoreCase(role)) {
            template = EmailTemplate.WELCOME_CLIENT;
            variables = Map.of("name", displayName);
        } else {
            throw new IllegalArgumentException(EmailConstants.ROLE_NOT_SUPPORTED_ERROR + role);
        }

        try {
            String content = emailTemplate.render(template, variables);
            sendHtmlEmail(recipientEmail, template.getSubject(), content);
        } catch (Exception e) {
            throw new RuntimeException(EmailConstants.SEND_WELCOME_ERROR, e);
        }
    }

    @Override
    public void sendPasswordRecoveryEmail(String recipientEmail, String displayName, String recoveryLink) {
        try {
            Map<String, Object> variables = Map.of(
                "name", displayName,
                "recoveryLink", recoveryLink
            );
            String content = emailTemplate.render(EmailTemplate.PASSWORD_RECOVERY, variables);
            sendHtmlEmail(recipientEmail, EmailTemplate.PASSWORD_RECOVERY.getSubject(), content);
        } catch (Exception e) {
            throw new RuntimeException(EmailConstants.SEND_RECOVERY_ERROR, e);
        }
    }

    @Override
    public void sendAccountBlockedEmail(String recipientEmail, String displayName, String recoveryLink) {
        try {
            Map<String, Object> variables = Map.of(
                "name", displayName,
                "recoveryLink", recoveryLink
            );
            String content = emailTemplate.render(EmailTemplate.ACCOUNT_BLOCKED, variables);
            sendHtmlEmail(recipientEmail, EmailTemplate.ACCOUNT_BLOCKED.getSubject(), content);
        } catch (Exception e) {
            throw new RuntimeException(EmailConstants.SEND_BLOCKED_ERROR, e);
        }
    }

    @Override
    public void sendAccountDeactivationEmail(String recipientEmail, String displayName, String token) {
        try {
            Map<String, Object> variables = Map.of(
                "name", displayName,
                "reactivationLink", UrlConstants.buildReactivationLink(token)
            );
            String content = emailTemplate.render(EmailTemplate.ACCOUNT_DEACTIVATION, variables);
            sendHtmlEmail(recipientEmail, EmailTemplate.ACCOUNT_DEACTIVATION.getSubject(), content);
        } catch (Exception e) {
            throw new RuntimeException(EmailConstants.SEND_DEACTIVATION_ERROR, e);
        }
    }

    @Override
    public void sendHtmlEmail(String to, String subject, String htmlBody) {
        try {
            String styledHtml = applyStyles(htmlBody);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());

            helper.setFrom(emailFromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(styledHtml, true);

            Resource logo = resourceLoader.getResource(EmailConstants.LOGO_IMAGE_PATH);
            helper.addInline("logo", logo);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException(EmailConstants.SEND_HTML_ERROR, e);
        }
    }

    private String applyStyles(String htmlContent) {
        try {
            Resource resource = resourceLoader.getResource("classpath:templates/css/templates.css");
            byte[] fileData = resource.getInputStream().readAllBytes();
            String commonStyles = new String(fileData, StandardCharsets.UTF_8);
            return htmlContent.replace("/* {{styles}} */", commonStyles);
        } catch (IOException e) {
            throw new RuntimeException(EmailConstants.LOAD_STYLESHEET_ERROR, e);
        }
    }
}