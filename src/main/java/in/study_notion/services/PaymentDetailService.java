package in.study_notion.services;

import in.study_notion.models.PaymentDetails;
import in.study_notion.models.User;

public interface PaymentDetailService {
    public PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String ifsc, String bankName, User user);
    public PaymentDetails getUserPaymentDetails(User user);
}
