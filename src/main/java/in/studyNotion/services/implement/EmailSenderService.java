package in.studyNotion.services.implement;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailSenderService {

    @Autowired
    private JavaMailSender javaMailSender;

    public boolean sendMail(String to,String subject,String body) {
        boolean isSend=false;
      try{
          SimpleMailMessage mailMessage = new SimpleMailMessage();
          mailMessage.setTo(to);
          mailMessage.setSubject(subject);
          mailMessage.setText(body);

          this.javaMailSender.send(mailMessage);
          isSend=true;

          return isSend;

      }catch (Exception e){
          log.error("Error Occurred while send email {} ",e.getMessage());
          return isSend;
      }
    }
}
