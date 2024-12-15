package in.study_notion.services;

import com.razorpay.RazorpayException;
import in.study_notion.models.PaymentOrder;
import in.study_notion.models.User;
import in.study_notion.responce.PaymentResponce;
import org.bson.types.ObjectId;

public interface PaymentOrderService {
    PaymentOrder createOrder(User user, Long amount);
    PaymentOrder getPaymentOrderById(ObjectId id);
    Boolean proccedPaymentOrder(PaymentOrder paymentOrder,String paymentId) throws RazorpayException;
    PaymentResponce createRazorpayPaymentLink(User user, Long amount,ObjectId courseId) throws RazorpayException;

}
