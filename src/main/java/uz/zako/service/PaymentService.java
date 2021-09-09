package uz.zako.service;

import uz.zako.entity.Payment;
import uz.zako.entity.User;

import java.util.List;
import java.util.UUID;

public interface PaymentService extends SuperService {

    List<Payment> findAllByUserId(User user);

    List<Payment> findAllByUserIdAndCategoryId(User user, UUID categoryId);

    public List<Payment> findAllByUserIdAndCategoryIdAndIncome(UUID userId, UUID categoryId, boolean income);



}
