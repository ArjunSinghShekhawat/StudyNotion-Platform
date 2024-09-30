package in.studyNotion;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;


@SpringBootApplication
@EnableTransactionManagement
public class StudyNotionApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudyNotionApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
	@Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

	@Bean
	public PlatformTransactionManager platformTransactionManager(MongoDatabaseFactory mongoDatabaseFactory){
		return new MongoTransactionManager(mongoDatabaseFactory);
	}

}
