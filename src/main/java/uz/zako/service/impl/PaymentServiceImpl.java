package uz.zako.service.impl;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.zako.entity.Payment;
import uz.zako.entity.User;
import uz.zako.exceptions.BadRequestException;
import uz.zako.exceptions.ResourceNotFoundException;
import uz.zako.model.Result;
import uz.zako.payload.PaymentPayload;
import uz.zako.repository.PaymentRepository;
import uz.zako.service.PaymentService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {


    private final PaymentRepository paymentRepository;
    private final UserServiceImpl userService;
    private final CategoryServiceImpl categoryService;


    public ResponseEntity<Payment> findByIdAndUser(UUID id, User user) {
        return ResponseEntity.ok(paymentRepository.findByIdAndUsers(id, user).orElseThrow(() -> new ResourceNotFoundException("Payment not found")));
    }


    public ResponseEntity savePayload(PaymentPayload paymentPayload, HttpServletRequest request) {
        User user = userService.whoAmI(request);

        if (user.getUsername().equals(paymentPayload.getUsername())) {
            if (categoryService.checkCategoryByIdAndUser(paymentPayload.getCategoryId(), user)) {

                return ResponseEntity.ok(paymentRepository.save(getPayment(paymentPayload, new Payment())));
            }
        }

        return ResponseEntity.badRequest().body("Paymentni yaratishga ruxsat yo'q");
    }

    @Override
    public List<Payment> findAllByUserId(User user) {
        return paymentRepository.findAllByUsers(user);
    }

    @Override
    public List<Payment> findAllByUserIdAndCategoryId(User user, UUID categoryId) {
        return paymentRepository.findAllByUsersAndCategory(user, categoryService.findById(categoryId));
    }

    @Override
    public List<Payment> findAllByUserIdAndCategoryIdAndIncome(UUID userId, UUID categoryId, boolean income) {
        return paymentRepository.findAllByUsersAndCategoryAndIncome(userService.findById(userId), categoryService.findById(categoryId), income);
    }

    @Override
    public <T> List findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public <T> T findById(UUID id) {
        return (T) paymentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

    }

    @Override
    public <T> T save(T t) {
        return (T) paymentRepository.save((Payment) t);
    }

    @Override
    public <T> T update(T t) {
        return this.save(t);
    }

    @Override
    public <T> boolean delete(UUID id) {

        paymentRepository.deleteById(id);
        return paymentRepository.existsById(id);
    }

    @Override
    public <T, R> T getObjectFromPayload(R r) {
        return null;
    }

    @Override
    public <T, R> T getPayloadFromObject(R r) {
        return null;
    }


    public Payment getPayment(PaymentPayload payload, Payment payment) {


        if (payload.getUsername() == null) {
            throw new BadRequestException("User id must be not null!");
        }
        if (payload.getAmount() == 0) {
            throw new BadRequestException("Qiymat 0 bo'lmasligi kerak!");
        }
        if (payload.getCategoryId() == null) {
            throw new BadRequestException("Category id must be not null!");
        }


        payment.setAmount(payload.getAmount());
        payment.setCategory(categoryService.findById(payload.getCategoryId()));
        payment.setIncome(payload.isIncome());
        payment.setDescription(payload.getDescription());
        payment.setUsers(userService.findByUsername(payload.getUsername()));


        if (payload.getReplayPaymentId() != null) {
            payment.setReplayPayment(paymentRepository.findById(payload.getReplayPaymentId()).orElse(null));
        }
        return payment;
    }


    public Payment editPayment(UUID id, PaymentPayload paymentPayload, User user) {
        Payment payment = paymentRepository.findByIdAndUsers(id, user).orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
        return getPayment(paymentPayload, payment);
    }

    public ResponseEntity<Result> deleteByIdAndUser(UUID id, User user) {
        Payment payment = findById(id);

        return paymentRepository.deleteByIdAndUsers(id, user)
                ? ResponseEntity.ok(new Result(true, "Payment successfully deleted!", null))
                : new ResponseEntity<>(new Result(false, "Some thing went wrong!", null), HttpStatus.CONFLICT);
    }

}
