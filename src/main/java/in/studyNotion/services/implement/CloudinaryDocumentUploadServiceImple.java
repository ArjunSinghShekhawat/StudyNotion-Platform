package in.studyNotion.services.implement;

import com.cloudinary.Cloudinary;
import in.studyNotion.enums.AccountTypes;
import in.studyNotion.models.User;
import in.studyNotion.repositories.UserRepository;
import in.studyNotion.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    public Map<?,?> upload(MultipartFile file,String jwt) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(4);

            Future<Map<String, Object>> future = executorService.submit(() -> {
                log.info("Enter in upload service successfully!");

                try {
                    String email = this.jwtUtils.getEmailFromToken(jwt);
                    User user = this.userRepository.findByEmail(email);

                    AccountTypes role = user.getAccountTypes();

                    // Folder selection based on account type
                    log.info("Select the folder path based on account type!");

                    String folder = "";

                    if(role.equals(AccountTypes.STUDENT)){
                        folder += "StudyNotion/Student";
                    }
                    else if(role.equals(AccountTypes.INSTRUCTOR)){
                        folder += "StudyNotion/Instructor";
                    }
                    else {
                        folder += "StudyNotion/Admin";
                    }

                    log.info("Folder path is {}", folder);

                    Map data = documentUploader(file, folder);

                    // Save user with the image URL
                    user.setImageUrl((String) data.get("secure_url"));
                    this.userRepository.save(user);

                    // Send email
                    boolean emailSend = this.emailSenderService.sendMail(user.getEmail(), "Your Document Uploaded", "Document Upload Successful!");
                    log.info("Email sent successfully: {}", emailSend);

                    // Return the data map
                    return data;

                } catch (Exception e) {
                    log.error("Error during file upload: ", e);
                    return Collections.emptyMap(); // Return empty map in case of error
                }
            });
            executorService.shutdown();

        Map<String, Object> map=null;
        try{
            map = future.get();
        }catch (InterruptedException | ExecutionException e) {
        log.error("Error while fetching the result from Future: ", e);
    }
        return map;
    }


    public Map documentUploader(MultipartFile file,String folder) throws Exception {

        try{
            // Fetch file name from the uploaded file
            log.info("Fetch the file name!");
            String originalFileName = file.getOriginalFilename();
            if (originalFileName != null && originalFileName.contains(".")) {
                originalFileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'));
            }
            log.info("The file name is {}", originalFileName);

            // Set options
            log.info("Set options!");
            Map<String, Object> options = new HashMap<>();
            options.put("folder", folder);
            options.put("resource_type", "auto");
            options.put("public_id", originalFileName);

            log.info("All options are {}", options);


            // Upload document to cloudinary
            Map<String, Object> data = this.cloudinary.uploader().upload(file.getBytes(), options);

            return data;
        }catch (Exception e){
            log.error("Error Occurred While Upload document in cloudinary {} ",e.getMessage());
            throw new Exception("Cloudinary error");
        }
    }
}
