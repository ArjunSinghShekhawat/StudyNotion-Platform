package in.study_notion.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GenerateToken {



    @Test
    @Disabled
    public void generateToken(){
        assertNotNull(in.study_notion.utils.GenerateToken.generateToken());
        System.out.println(in.study_notion.utils.GenerateToken.generateToken());
    }
}
