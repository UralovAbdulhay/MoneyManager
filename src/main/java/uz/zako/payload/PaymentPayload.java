package uz.zako.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import uz.zako.entity.Payment;
import uz.zako.service.CategoryService;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentPayload {


    private double amount;
    private boolean income;
    private String username;
    private UUID categoryId;
    private UUID replayPaymentId;
    private String description;




}


















