package in.study_notion.exceptions;
public class CloudinaryUploadException extends Exception {
    public CloudinaryUploadException(String message, Throwable cause) {
        super(message, cause);
    }

    public CloudinaryUploadException(String message) {
        super(message);
    }
}
