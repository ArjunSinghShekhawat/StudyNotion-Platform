package in.study_notion.models;

import in.study_notion.enums.PaymentOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentOrder {

    @Id
    private ObjectId id;

    private Long amount;

    private PaymentOrderStatus paymentOrderStatus;

    @DBRef
    private User users;
}
