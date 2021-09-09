package uz.zako.payload;


import lombok.Data;

@Data
public class UserPayload {

    private String fullName;

    private String username;

    private String password;

    private String phone;

    private String email;


}
