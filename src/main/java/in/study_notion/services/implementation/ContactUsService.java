
package in.study_notion.services.implementation;


import in.study_notion.models.ContactUs;
import in.study_notion.repositories.ContactUsRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ContactUsService {

    private final ContactUsRepository contactUsRepository;

    @Autowired
    public ContactUsService(ContactUsRepository contactUsRepository) {
        this.contactUsRepository = contactUsRepository;
    }
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);


    public boolean contactUs(ContactUs contactUs){

        /*
        This method is used for contact us message
         */
        boolean isSaved = false;

        try{
            //creation
            ContactUs newContactUs = new ContactUs();

            newContactUs.setCountryCode(contactUs.getCountryCode());
            newContactUs.setEmail(contactUs.getEmail());
            newContactUs.setFirstName(contactUs.getFirstName());
            newContactUs.setLastName(contactUs.getLastName());
            newContactUs.setPhoneNumber(contactUs.getPhoneNumber());
            newContactUs.setMessage(contactUs.getMessage());

            ContactUs saveContactUs = this.contactUsRepository.save(newContactUs);
            isSaved=true;

            scheduleDelete(saveContactUs.getId());
        }catch (Exception e){

            log.error("Error occurred while create contact us");
        }

        return isSaved;
    }
    // Method to schedule the deletion
    private void scheduleDelete(ObjectId contactUsId) {
        // Schedule task to delete after 5 minutes
        scheduler.schedule(() -> {
            try {
                deleteContactUs(contactUsId);
                shutdownScheduler();
            } catch (Exception e) {
                // Handle exceptions from deleteContactUs if needed
                log.error("Contact us delete error {} ",e.getMessage());
            }
        }, 5, TimeUnit.MINUTES); // Change to 5 minutes as per your comment
    }
    private void shutdownScheduler() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    // Actual deletion logic
    private void deleteContactUs(ObjectId contactUsId) {
        try {
            contactUsRepository.deleteById(contactUsId);
           log.info("ContactUs record deleted successfully after 5 minutes");
        } catch (Exception e) {
            log.error("Error Occurred while delete contact us details {} ",e.getMessage());
        }
    }

}