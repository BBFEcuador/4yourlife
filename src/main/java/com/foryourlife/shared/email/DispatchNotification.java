package com.foryourlife.shared.email;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DispatchNotification {
    private final SendGrid sendGrid;
    private final String fromEmail;

    public DispatchNotification(
            @Value("${com.email.sendgrid.api-key}") String sendgridApiKey,
            @Value("${com.email.sendgrid.from-email}") String fromEmail) {
        this.sendGrid = new SendGrid(sendgridApiKey);
        this.fromEmail = fromEmail;
    }

    public void send(String toEmail, String subject, String message) {
        try {
            Email to = new Email(toEmail);
            Email from = new Email(fromEmail);

            Content content = new Content("text/html", message);
            Mail mail = new Mail(from, subject, to, content);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            System.out.println("Correo enviado. Código de estado: " + response.getStatusCode());
            System.out.println("Respuesta de SendGrid: " + response.getBody());

        } catch (IOException e) {
            System.out.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}