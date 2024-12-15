package in.study_notion.controllers;

import in.study_notion.models.PaymentDetails;
import in.study_notion.models.User;
import in.study_notion.services.PaymentDetailService;
import in.study_notion.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class PaymentDetailsController {

    @Autowired
    private PaymentDetailService paymentDetailService;

    @Autowired
    private UserService userService;

    @PostMapping("/payment-details")
    public ResponseEntity<PaymentDetails> addPaymentDetails(@RequestBody PaymentDetails paymentDetailsReq,
                                                            @RequestHeader("Authorization") String jwt){

        User user = this.userService.getUserAllInformationByJwt(jwt);
        PaymentDetails paymentDetails = this.paymentDetailService.addPaymentDetails(
                paymentDetailsReq.getAccountNumber(),
                paymentDetailsReq.getAccountHolderName(),
                paymentDetailsReq.getIfsc(),
                paymentDetailsReq.getBankName(),
                user
        );

        return new ResponseEntity<>(paymentDetails, HttpStatus.CREATED);
    }
    @GetMapping("/payment-details")
    public ResponseEntity<PaymentDetails>getUserPaymentDetails(@RequestHeader("Authorization") String jwt){
        User user = this.userService.getUserAllInformationByJwt(jwt);
        PaymentDetails paymentDetails = this.paymentDetailService.getUserPaymentDetails(user);
        return new ResponseEntity<>(paymentDetails,HttpStatus.OK);
    }
}
