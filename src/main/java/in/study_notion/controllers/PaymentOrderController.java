package in.study_notion.controllers;

import com.razorpay.RazorpayException;
import in.study_notion.models.PaymentOrder;
import in.study_notion.models.User;
import in.study_notion.responce.PaymentResponce;
import in.study_notion.services.PaymentOrderService;
import in.study_notion.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class PaymentOrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentOrderService paymentOrderService;

    @PostMapping("/amount/{amount}")
    public ResponseEntity<PaymentResponce> paymentHandler(
            @PathVariable Long amount,
            @RequestHeader("Authorization") String jwt
    ) throws RazorpayException{
        User user = this.userService.getUserAllInformationByJwt(jwt);
        PaymentResponce paymentResponce;

        PaymentOrder order = this.paymentOrderService.createOrder(user,amount);

        paymentResponce  = this.paymentOrderService.createRazorpayPaymentLink(user,amount,order.getId());

        return new ResponseEntity<>(paymentResponce, HttpStatus.CREATED);
    }
}
