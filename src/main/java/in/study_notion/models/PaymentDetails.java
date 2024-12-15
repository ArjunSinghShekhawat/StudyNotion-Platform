package in.study_notion.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {
    @Id
    private ObjectId id;

    private String accountNumber;
    private String accountHolderName;
    private String ifsc;
    private String bankName;

    @DBRef
    private User user;
}
