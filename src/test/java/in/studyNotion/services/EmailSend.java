package in.studyNotion.services;

import in.studyNotion.services.implement.EmailSenderService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class EmailSend {


    @Autowired
    private EmailSenderService emailSenderService;


    @Disabled
    @ParameterizedTest
    @CsvSource({"shekhawat2804@gmail.com, \"Testing purpose\", \"Hy arjun\""})
    public void sendMail(String email, String sub, String body) {
        assertNotNull(this.emailSenderService.sendMail(email, sub, body));
        System.out.println(this.emailSenderService.sendMail(email, sub, body));
    }

}
