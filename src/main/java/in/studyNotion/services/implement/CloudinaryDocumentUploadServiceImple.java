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

            String originalFileName = file.getOriginalFilename();


            if (originalFileName != null && !originalFileName.isEmpty()) {
                log.info("Original file name: " + originalFileName);
            } else {
                throw new Exception ("File name is invalid or file is empty!");
            }

            //check file type
            if (originalFileName.endsWith(".jpg") || originalFileName.endsWith(".png") || originalFileName.endsWith(".pdf")) {

                 options.put("format","jpg");
                 options.put("width","800");
                 options.put("height","600");

            } else if (originalFileName.endsWith(".mp4") || originalFileName.endsWith(".avi") || originalFileName.endsWith(".mov")) {

                options.put("format","mp4");
                options.put("width","1280");
                options.put("height","720");

            } else {
                throw new IllegalArgumentException("Unsupported file type. Please upload an image or video.");
            }


            log.info("All options are {}", options);

            // Upload document to cloudinary

            return this.cloudinary.uploader().upload(file.getBytes(), options);

        } catch (Exception e) {
            log.error("Error during file upload: ", e);
            throw new RuntimeException("Cloudinary Exception !");
        }
    }

}
