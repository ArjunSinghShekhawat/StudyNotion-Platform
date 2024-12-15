package in.study_notion.services.implementation;

import in.study_notion.models.PaymentDetails;
import in.study_notion.models.User;
import in.study_notion.repositories.PaymentDetailsRepository;
import in.study_notion.services.PaymentDetailService;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentDetailsImpl implements PaymentDetailService {

    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;

    @Override
    public PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String ifsc, String bankName, User user) {

        PaymentDetails paymentDetails = new PaymentDetails();

        paymentDetails.setBankName(bankName);
        paymentDetails.setIfsc(ifsc);
        paymentDetails.setAccountHolderName(accountHolderName);
        paymentDetails.setAccountNumber(accountNumber);
        paymentDetails.setUser(user);

        return this.paymentDetailsRepository.save(paymentDetails);
    }

    @Override
    public PaymentDetails getUserPaymentDetails(User user) {
        return this.paymentDetailsRepository.findByUserId(user.getId());
    }
}
