package uz.zako.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zako.model.Result;
import uz.zako.payload.UserPayload;
import uz.zako.service.impl.PaymentServiceImpl;
import uz.zako.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserServiceImpl userService;
    private final PaymentServiceImpl paymentService;

    // admin tomonidan qo'shilagi
    @PostMapping("/user/add")
    public ResponseEntity addUser(@RequestBody UserPayload userPayload) {
        return ResponseEntity.ok(userService.create(userPayload));
    }


    @DeleteMapping("/user/{id}")
    public ResponseEntity<Result> delete(@PathVariable UUID id) {
        return userService.delete(id)
                ? ResponseEntity.ok(new Result(true, "deleted", null))
                : new ResponseEntity<>(new Result(false, "not deleted", null), HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/user/update")
    public ResponseEntity updateUser(UserPayload userPayload, HttpServletRequest request) {
        return userService.updateUserByAdmin(userPayload, request);
    }


    @GetMapping("/user/{id}")
    public ResponseEntity getUserById(@PathVariable UUID id) {
        return new ResponseEntity(userService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/user/all")
    public ResponseEntity getAll() {
        return ResponseEntity.ok(userService.findAll());
    }


    @GetMapping("/user/payments")
    public ResponseEntity getAllPayments(HttpServletRequest request) {
        return ResponseEntity.ok(paymentService.findAllByUserId(userService.whoAmI(request)));
    }



}
