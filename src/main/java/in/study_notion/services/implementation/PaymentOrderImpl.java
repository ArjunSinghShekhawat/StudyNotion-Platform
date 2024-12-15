package in.study_notion.services.implementation;

import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import in.study_notion.constants.Constant;
import in.study_notion.enums.PaymentOrderStatus;
import in.study_notion.exceptions.ResponceNotFoundException;
import in.study_notion.models.PaymentOrder;
import in.study_notion.models.User;
import in.study_notion.repositories.PaymentOrderRepository;
import in.study_notion.responce.PaymentResponce;
import in.study_notion.services.PaymentOrderService;
import org.bson.types.ObjectId;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentOrderImpl implements PaymentOrderService {

    @Autowired
    private PaymentOrderRepository paymentOrderRepository;



    @Override
    public PaymentOrder createOrder(User user, Long amount) {
        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.PENDING);
        paymentOrder.setUsers(user);
        paymentOrder.setAmount(amount);
        return this.paymentOrderRepository.save(paymentOrder);
    }

    @Override
    public PaymentOrder getPaymentOrderById(ObjectId id) {
       return this.paymentOrderRepository.findById(id).orElseThrow(()->new ResponceNotFoundException("PaymentOrder","id",id+""));
    }

    @Override
    public Boolean proccedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws RazorpayException {
        if (paymentOrder == null || paymentId == null) {
            return false;
        }
        if (paymentOrder.getPaymentOrderStatus().equals(PaymentOrderStatus.PENDING)) {
            RazorpayClient razorpayClient = new RazorpayClient(Constant.RAZORPAY_API_KEY, Constant.RAZORPAY_API_SECRET);
            Payment payment = razorpayClient.payments.fetch(paymentId);

            Integer amount = payment.get("amount");
            String status = payment.get("status");

            if ("captured".equals(status)) {
                paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.SUCCESS);
                paymentOrderRepository.save(paymentOrder);
                return true;
            } else {
                paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.FAILED);
            }
        } else {
            paymentOrder.setPaymentOrderStatus(PaymentOrderStatus.SUCCESS);
        }
        paymentOrderRepository.save(paymentOrder);
        return paymentOrder.getPaymentOrderStatus().equals(PaymentOrderStatus.SUCCESS);

    }

    @Override
    public PaymentResponce createRazorpayPaymentLink(User user, Long amount, ObjectId courseId) throws RazorpayException {
        Long Amount = amount * 100;

        try {
            RazorpayClient razorpayClient = new RazorpayClient(Constant.RAZORPAY_API_KEY, Constant.RAZORPAY_API_SECRET);
            JSONObject paymentLinkRequest = new JSONObject();
            paymentLinkRequest.put("amount", Amount);
            paymentLinkRequest.put("currency", "INR");

            JSONObject customer = new JSONObject();
            customer.put("name", user.getFirstName()+user.getLastName());
            customer.put("email", user.getEmail());
            paymentLinkRequest.put("customer", customer);

            JSONObject notify = new JSONObject();
            notify.put("email", true);
            paymentLinkRequest.put("notify", notify);

            paymentLinkRequest.put("reminder_enable", true);
            paymentLinkRequest.put("callback_url","http://localhost:5173/wallet?orderId=" + courseId);
            paymentLinkRequest.put("callback_method", "get");

            PaymentLink payment = razorpayClient.paymentLink.create(paymentLinkRequest);
            String paymentLinkId = payment.get("id");
            String paymentLinkUrl = payment.get("short_url");

            PaymentResponce paymentResponse = new PaymentResponce();
            paymentResponse.setPaymentUrl(paymentLinkUrl);
            return paymentResponse;
        } catch (RazorpayException e) {
            System.out.println("Error creating payment link: " + e.getMessage());
            throw new RazorpayException(e.getMessage());
        }
    }
}
