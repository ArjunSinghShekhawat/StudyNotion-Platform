package in.studyNotion.services.implement;

import com.cloudinary.Cloudinary;
import in.studyNotion.repositories.UserRepository;
import in.studyNotion.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Slf4j
@Service
public class CloudinaryDocumentUploadServiceImple {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailSenderService emailSenderService;


    @Transactional
    public Map<?,?> upload(MultipartFile file,String folder,String fileName) throws ExecutionException, InterruptedException {

        log.info("Enter in upload service successfully!");

        try {
            //folder
            log.info("Folder path is {}", folder);

            // Set options
            log.info("Set options!");
            Map<String, Object> options = new HashMap<>();

            options.put("folder", folder);
            options.put("resource_type", "auto");
            options.put("public_id", fileName);

            log.info("All options are {}", options);

            // Upload document to cloudinary

            return this.cloudinary.uploader().upload(file.getBytes(), options);

        } catch (Exception e) {
            log.error("Error during file upload: ", e);
            throw new RuntimeException("Cloudinary Exception !");
        }
    }

}
