package in.study_notion.services.implementation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender){
        this.javaMailSender=javaMailSender;
    }

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public boolean sendMail(String to,String subject,String body) {
        boolean isSend=false;
      try{
          SimpleMailMessage mailMessage = new SimpleMailMessage();
          mailMessage.setTo(to);
          mailMessage.setSubject(subject);
          mailMessage.setText(body);


          executorService.submit(()-> this.javaMailSender.send(mailMessage));
          executorService.shutdown();

          isSend=true;

          return isSend;

      }catch (Exception e){
          log.error("Error Occurred while send email {} ",e.getMessage());
          return isSend;
      }
    }
}
