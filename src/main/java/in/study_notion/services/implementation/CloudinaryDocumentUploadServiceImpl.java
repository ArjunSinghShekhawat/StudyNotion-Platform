package in.study_notion.services.implementation;

import com.cloudinary.Cloudinary;
import in.study_notion.constants.Constant;
import in.study_notion.exceptions.CloudinaryUploadException;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class CloudinaryDocumentUploadServiceImpl {



    private final Cloudinary cloudinary;

    @Autowired
    public CloudinaryDocumentUploadServiceImpl(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    @Transactional
    public Map<?, ?> upload(MultipartFile file, String folder, String fileName)
            throws IOException, CloudinaryUploadException {
        log.info("Enter in upload service successfully!");

        // Validate folder and fileName
        if (folder == null || folder.isEmpty()) {
            throw new IllegalArgumentException("Folder path cannot be null or empty.");
        }
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty.");
        }

        log.info("Folder path is {}", folder);
        log.info("Set options!");

        Map<String, Object> options = new HashMap<>();
        options.put("folder", folder);
        options.put("resource_type", "auto");
        options.put("public_id", fileName);

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new IllegalArgumentException("File name is invalid or file is empty!");
        }
        log.info("Original file name: {}", originalFileName);

        // Check file type
        if (isSupportedImageType(originalFileName)) {
            options.put("format", Constant.DEFAULT_IMAGE_FORMAT);
            options.put("width", Constant.IMAGE_WIDTH);
            options.put("height", Constant.IMAGE_HEIGHT);
        } else if (isSupportedVideoType(originalFileName)) {
            options.put("format", Constant.DEFAULT_VIDEO_FORMAT);
            options.put("width", Constant.VIDEO_WIDTH);
            options.put("height", Constant.VIDEO_HEIGHT);
        } else {
            throw new UnsupportedOperationException("Unsupported file type. Please upload an image or video.");
        }

        log.info("All options are {}", options);

        // Upload document to Cloudinary
        try {
            return cloudinary.uploader().upload(file.getBytes(), options);
        } catch (IOException e) {
            log.error("Error during file upload: ", e);
            throw new CloudinaryUploadException("Cloudinary upload failed.", e);
        }
    }

    private boolean isSupportedImageType(String fileName) {
        for (String type : Constant.SUPPORTED_IMAGE_TYPES) {
            if (fileName.endsWith(type)) {
                return true;
            }
        }
        return false;
    }

    private boolean isSupportedVideoType(String fileName) {
        for (String type : Constant.SUPPORTED_VIDEO_TYPES) {
            if (fileName.endsWith(type)) {
                return true;
            }
        }
        return false;
    }
}
