package spring.labserver.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import spring.labserver.error.exception.MailMessagingException;

@RequiredArgsConstructor
@Service
public class MailService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to, String newPassword) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom(from);
            messageHelper.setTo(to);
            messageHelper.setSubject("[네트워크보안연구실] 임시 비밀번호 발급 안내");

            StringBuilder body = new StringBuilder();
            body.append("[네트워크보안연구실] 임시 비밀번호 발급 안내 메일입니다.");
            body.append("<br><br>");
            body.append("발급 받으신 비밀번호는 '<b>" + newPassword + "</b>' 입니다.");
            body.append("<br><br>");
            body.append("해당 비밀번호로 로그인 후 반드시 비밀번호를 변경해 주세요.");
            messageHelper.setText(body.toString(), true);

            mailSender.send(mimeMessage);
            logger.info("MailService sendMail() success");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.info("MailService sendMail() fail :" + e);
            throw new MailMessagingException();
        }

    }
    
}
