package uz.zako.payload;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentRequest {
    private UUID categoryId;
    private boolean income;
}
