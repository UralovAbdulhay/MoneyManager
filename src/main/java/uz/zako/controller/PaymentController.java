package uz.zako.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zako.entity.User;
import uz.zako.payload.PaymentPayload;
import uz.zako.payload.PaymentRequest;
import uz.zako.service.impl.CategoryServiceImpl;
import uz.zako.service.impl.PaymentServiceImpl;
import uz.zako.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final UserServiceImpl userService;
    private final PaymentServiceImpl paymentService;
    private final CategoryServiceImpl categoryService;

    /*

        getAll +
        getOne +
        create  +
        getAllByCategory  +
        getAllByCategoryAndIncome +
        edit   +
        delete +


    */


    @GetMapping("/all")
    public ResponseEntity getAllPayments(HttpServletRequest request) {
        return ResponseEntity.ok(paymentService.findAllByUserId(userService.whoAmI(request)));
    }

    @GetMapping("/{id}")
    public ResponseEntity getPayment(@PathVariable UUID id, HttpServletRequest request) {
        User user = userService.whoAmI(request);
        return ResponseEntity.ok(paymentService.findByIdAndUser(id, user));
    }

    @PostMapping("/add")
    public ResponseEntity createPayment(@RequestBody PaymentPayload paymentPayload, HttpServletRequest request) {

        return paymentService.savePayload(paymentPayload, request);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity getAllByCategory(@PathVariable UUID categoryId, HttpServletRequest request) {
        return ResponseEntity.ok(paymentService.findAllByUserIdAndCategoryId(userService.whoAmI(request), categoryId));
    }


    @GetMapping("/category-income")
    public ResponseEntity getAllByCategoryAndIncome(@RequestBody PaymentRequest paymentRequest, HttpServletRequest
            request) {
        return ResponseEntity.ok(paymentService.findAllByUserIdAndCategoryIdAndIncome(
                userService.whoAmI(request).getId(),
                paymentRequest.getCategoryId(),
                paymentRequest.isIncome()
                )
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity editPayment(@PathVariable UUID id, PaymentPayload paymentPayload, HttpServletRequest
            request) {
        return ResponseEntity.ok(paymentService.editPayment(id, paymentPayload, userService.whoAmI(request)));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity responseEntity(@PathVariable UUID id, HttpServletRequest request) {
        return paymentService.deleteByIdAndUser(id, userService.whoAmI(request));
    }

}
