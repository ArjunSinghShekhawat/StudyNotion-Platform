package in.studyNotion.services;

import in.studyNotion.utils.JwtUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GetEmail {

    @Autowired
    private JwtUtils jwtUtils;

    @Disabled
    @ParameterizedTest
    @CsvSource("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhYmhpQGdtYWlsLmNvbSIsImlhdCI6MTcyNzQzODU5MiwiZXhwIjoxNzI3NTI0OTkyfQ.9VBMGMdP2uYXfq0kC1Q4LbfoPWkS496lG-WRR8LkhuA")
    public void extractUsername(String token){
        assertNotNull(this.jwtUtils.getEmailFromToken(token));
        System.out.println(this.jwtUtils.getEmailFromToken(token));
    }
}
