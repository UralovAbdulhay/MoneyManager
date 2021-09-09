package uz.zako.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zako.model.Result;
import uz.zako.model.ResultSucces;
import uz.zako.payload.UserPayload;
import uz.zako.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserServiceImpl userService;


    @PostMapping("/register")
    public ResponseEntity register(@RequestBody UserPayload userPayload) {
        return userService.create(userPayload);
    }


    @PutMapping("/reset-password")
    public ResponseEntity<ResultSucces> resetPassword(HttpServletRequest request) {
        return ResponseEntity.ok(userService.resetPassword(request));
    }

    // 95 020 26 62

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserPayload userPayload) {
//        return ResponseEntity.ok(userService.login(userPayload));
        return ResponseEntity.ok(new Result(true, "O'xsahdi", userService.login(userPayload)));
    }


    @GetMapping("/getme")
    public ResponseEntity getUser(HttpServletRequest request) {
        return userService.getUser(request);
    }


}
