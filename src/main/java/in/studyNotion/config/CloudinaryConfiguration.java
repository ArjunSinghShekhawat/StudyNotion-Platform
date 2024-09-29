package in.studyNotion.config;

import com.cloudinary.Cloudinary;
import in.studyNotion.constants.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class CloudinaryConfiguration {

    @Bean
    public Cloudinary cloudinary(){
        final Map config = new HashMap<>();

        config.put("cloud_name", Constant.CLOUD_NAME);
        config.put("api_key",Constant.API_KEY);
        config.put("api_secret",Constant.API_SECRET);
        config.put("secure",Constant.SECURE);

        log.info("cloud configuration {} ",config);

        return new Cloudinary(config);
    }
}
