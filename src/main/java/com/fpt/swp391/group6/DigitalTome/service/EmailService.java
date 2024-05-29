package com.fpt.swp391.group6.DigitalTome.service;

import com.fpt.swp391.group6.DigitalTome.entity.BookEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    @Autowired
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendHtmlEmail(String to, String code) throws MessagingException {
        String htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1, shrink-to-fit=no\">\n" +
                "    <title>Email Verification</title>\n" +
                "    <!-- Bootstrap CSS -->\n" +
                "    <link href=\"https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css\" rel=\"stylesheet\">\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <div class=\"row justify-content-center\">\n" +
                "            <div class=\"col-md-6 mt-5\">\n" +
                "                <div class=\"card\">\n" +
                "                    <div class=\"card-body\">\n" +
                "                        <p class=\"card-text\">Mã xác nhận của bạn là:  <b>"+code+"</b> </p>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject("Mã xác nhận");
        helper.setText(htmlContent, true);
        mailSender.send(message);
    }

    public void sendNewBookNotification(String to, List<BookEntity> newBooks) throws MessagingException {
        String subject = "New Books Available!";
        StringBuilder messageContent = new StringBuilder("<html><body>");
        messageContent.append("<h1>Dear User,</h1>")
                .append("<p>The following new books are now available:</p>")
                .append("<ul>");

        for (BookEntity book : newBooks) {
            messageContent.append("<li>")
                    .append("<strong>Title:</strong> ").append(book.getTitle()).append("<br>")
                    .append("<strong>Description:</strong> ").append(book.getDescription())
                    .append("</li><br>");
        }

        messageContent.append("</ul>")
                .append("<p>Best regards,</p>")
                .append("<p>Digital Tome Team</p>")
                .append("</body></html>");

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(messageContent.toString(), true);

        mailSender.send(message);
    }
}


