package in.study_notion.services;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class Profile {

    @Autowired
    private ProfileService profileService;

    @Disabled
    @ParameterizedTest
    @CsvSource("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrb21hbEBnbWFpbC5jb20iLCJpYXQiOjE3Mjc0NDc2NDcsImV4cCI6MTcyNzUzNDA0N30.JnJxUdIn97B9B2TwOoIzngQRbpTT-YM7ZNljaAywnjA")
    public void getUserProfile(String token){
        assertNotNull(this.profileService.getUserProfile(token));
        System.out.println(this.profileService.getUserProfile(token));
    }
}
