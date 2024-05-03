package dev.azdanov.notificationservice.domain;

import static java.nio.charset.StandardCharsets.UTF_8;

import dev.azdanov.notificationservice.ApplicationProperties;
import dev.azdanov.notificationservice.domain.models.OrderCancelledEvent;
import dev.azdanov.notificationservice.domain.models.OrderCreatedEvent;
import dev.azdanov.notificationservice.domain.models.OrderDeliveredEvent;
import dev.azdanov.notificationservice.domain.models.OrderEvent;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender emailSender;
    private final ApplicationProperties properties;

    public NotificationService(JavaMailSender emailSender, ApplicationProperties properties) {
        this.emailSender = emailSender;
        this.properties = properties;
    }

    public void sendNotification(OrderEvent event, NotificationTemplate template) {
        String recipient = getRecipient(event);
        String title = template.getTitle();
        String body = formatMessage(event, template.getMessageBody());
        sendEmail(recipient, title, body);
    }

    private String getRecipient(OrderEvent event) {
        return switch (event) {
            case OrderCreatedEvent orderCreated -> orderCreated.getRecipientEmail();
            case OrderDeliveredEvent orderDelivered -> orderDelivered.getRecipientEmail();
            case OrderCancelledEvent orderCancelled -> orderCancelled.getRecipientEmail();
            default -> properties.supportEmail();
        };
    }

    private String formatMessage(OrderEvent event, String template) {
        return template.formatted(event.getRecipientName(), event.orderNumber(), event.getReason());
    }

    private void sendEmail(String recipient, String title, String body) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, UTF_8.name());
            helper.setFrom(properties.supportEmail());
            helper.setTo(recipient);
            helper.setSubject(title);
            helper.setText(body);
            emailSender.send(mimeMessage);
            log.info("Email sent to {}", recipient);
        } catch (Exception e) {
            throw new NotificationException("Error while sending email", e);
        }
    }
}
