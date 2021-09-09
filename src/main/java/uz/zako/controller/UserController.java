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
@RequestMapping("api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;
    private final PaymentServiceImpl paymentService;


    @PutMapping("/user/update-details")
    public ResponseEntity edit(@RequestBody UserPayload user, HttpServletRequest request) {
        return new ResponseEntity(userService.updateDetails(user, userService.whoAmI(request)), HttpStatus.OK);
    }






}



























