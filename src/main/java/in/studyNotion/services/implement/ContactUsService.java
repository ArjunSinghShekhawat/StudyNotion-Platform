
package in.studyNotion.services.implement;


import in.studyNotion.models.ContactUs;
import in.studyNotion.repositories.ContactUsRepository;
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

    @Autowired
    private ContactUsRepository contactUsRepository;

    @Autowired
    private EmailSenderService emailSenderService;

    public boolean contactUs(ContactUs contactUs){

        boolean isSaved = false;

        try{
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
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        // Schedule task to delete after 5 minutes
        scheduler.schedule(() -> {
            try {
                deleteContactUs(contactUsId);
            } finally {
                // Properly shutdown the scheduler after the task
                scheduler.shutdown();
            }
        }, 1, TimeUnit.MINUTES);
    }

    // Actual deletion logic
    private void deleteContactUs(ObjectId contactUsId) {
        try {
            contactUsRepository.deleteById(contactUsId);
            System.out.println("ContactUs record deleted successfully after 5 minutes");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}