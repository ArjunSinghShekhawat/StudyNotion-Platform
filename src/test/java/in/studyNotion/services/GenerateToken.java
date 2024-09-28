package in.studyNotion.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GenerateToken {



    @Test
    @Disabled
    public void generateToken(){
        assertNotNull(in.studyNotion.utils.GenerateToken.generateToken());
        System.out.println(in.studyNotion.utils.GenerateToken.generateToken());
    }
}
