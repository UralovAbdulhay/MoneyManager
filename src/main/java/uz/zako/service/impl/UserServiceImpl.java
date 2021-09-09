package uz.zako.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.zako.entity.Role;
import uz.zako.entity.User;
import uz.zako.exceptions.BadRequestException;
import uz.zako.exceptions.ResourceNotFoundException;
import uz.zako.model.Result;
import uz.zako.model.ResultSucces;
import uz.zako.payload.UserPayload;
import uz.zako.repository.RoleRepository;
import uz.zako.repository.UserRepository;
import uz.zako.security.JwtTokenProvider;
import uz.zako.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public <T> List findAll() {
        return userRepository.findAll();
    }

    @Override
    public <T> T findById(UUID id) {
        return (T) userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public <T> T save(T t) {
        return (T) userRepository.save((User) t);
    }


    @Override
    public <T> T update(T t) {
        return this.save(t);
    }

    @Override
    public <T> boolean delete(UUID id) {
        User user = findById(id);
        user.setActive(false);
        userRepository.save(user);
        return userRepository.existsByIdAndActive(id, false);
    }

    @Override
    public <T, R> T getObjectFromPayload(R payload) {
        UserPayload userPayload = (UserPayload) payload;
        User user = new User();

        user.setUsername(userPayload.getUsername());
        user.setPassword(userPayload.getPassword());

        return (T) user;
    }

    @Override
    public <T, R> T getPayloadFromObject(R object) {
        User user = (User) object;
        UserPayload userPayload = new UserPayload();
        userPayload.setPassword(user.getPassword());
        userPayload.setUsername(user.getUsername());
        userPayload.setFullName(user.getFullName());
        userPayload.setPhone(user.getPhone());
        userPayload.setEmail(user.getEmail());


        return (T) userPayload;
    }


    public ResponseEntity create(UserPayload userPayload) {
        User user = new User();

        if (checkUsername(userPayload.getUsername())) {
            return new ResponseEntity(
                    new Result(false, "Bu username  ro'yxatdan o'tgan", null),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (checkEmail(userPayload.getEmail())) {
            return new ResponseEntity(
                    new Result(false, "Bu email  ro'yxatdan o'tgan", null),
                    HttpStatus.BAD_REQUEST
            );
        }

        if (checkPhone(userPayload.getPhone())) {
            return new ResponseEntity(
                    new Result(false, "Telefon nomer ro'yxatdan o'tgan", null),
                    HttpStatus.BAD_REQUEST
            );
        }
        if (!checkPasswordLength(userPayload.getPassword())) {
            return new ResponseEntity(
                    new Result(false, "Parol uzunligi 5 dan kam", null),
                    HttpStatus.BAD_REQUEST
            );
        }

        user.setPassword(passwordEncoder.encode(userPayload.getPassword()));
        user.setFullName(userPayload.getFullName());
        user.setUsername(userPayload.getUsername());
        user.setPhone(userPayload.getPhone());
        user.setEmail(userPayload.getEmail());

        ArrayList<Role> roles = new ArrayList<>(1);

        if (!roleRepository.existsByName("USER")){
            roleRepository.save(new Role("USER"));
        }

        user.setRoles(roleRepository.findAllByName("USER"));

        return ResponseEntity.ok(userRepository.save(user));
    }


    public User whoAmI(HttpServletRequest req) {
        try {
            return userRepository.findByUsername(jwtTokenProvider.getUser(jwtTokenProvider.resolveToken(req)))
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        } catch (Exception e) {
            return null;
        }
    }


    public ResponseEntity<ResultSucces> updateDetails(UserPayload userPayload, User me) {


        if (!me.getUsername().equals(userPayload.getUsername())) {
            if (checkUsername(userPayload.getUsername())) {
                return new ResponseEntity(new ResultSucces(false, "Bu username band"), BAD_REQUEST);
            }
        }

        if (!me.getEmail().equals(userPayload.getEmail())) {
            if (checkEmail(userPayload.getEmail())) {
                return new ResponseEntity(new ResultSucces(false, "Bu email band"), BAD_REQUEST);
            }
        }

        if (!me.getPhone().equals(userPayload.getPhone())) {
            if (checkPhone(userPayload.getPhone())) {
                return new ResponseEntity(new ResultSucces(false, "Bu phone band"), BAD_REQUEST);
            }
        }

        me.setUsername(userPayload.getUsername());
        me.setEmail(userPayload.getEmail());
        me.setPhone(userPayload.getPhone());
        me.setFullName(userPayload.getFullName());

        me = update(me);

        String token = jwtTokenProvider.createToken(me.getUsername(), me.getRoles());

        Map<Object, Object> result = new HashMap<>(3);
        result.put("success", true);
        result.put("username", me.getUsername());
        result.put("token", token);

        return new ResponseEntity(new ResultSucces(true, result), OK);
    }


    // profildan turib password ni re-set qilish
    public ResultSucces resetPassword(HttpServletRequest request) {

        User user = whoAmI(request);
        String newPassword = request.getHeader("newPassword");

        if (newPassword != null && checkPasswordLength(newPassword.trim())) {
            newPassword = newPassword.trim();
            user.setPassword(passwordEncoder.encode(newPassword));
            user = update(user);

            String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());

            Map<Object, Object> map = new HashMap<>(4);
            map.put("success", true);
            map.put("username", user.getUsername());
            map.put("token", token);
            return new ResultSucces(true, map);
        }
        return new ResultSucces(false, new BadRequestException("Password not changed!"));
    }

    public ResultSucces login(UserPayload userPayload) {

        User user = findByUsername(userPayload.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userPayload.getUsername(), userPayload.getPassword()
                )
        );

        String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());

        Map<Object, Object> map = new HashMap<>(3);
        map.put("success", true);
        map.put("username", user.getUsername());
        map.put("token", token);

        return new ResultSucces(true, map);
    }


    public ResponseEntity<ResultSucces> getUser(HttpServletRequest request) {
        User user = whoAmI(request);
        return user != null ? ResponseEntity.ok(new ResultSucces(true, user))
                : (new ResponseEntity(new Result(false, "token is invalid", null), BAD_REQUEST));
    }


    // admin userlarni update qilishi
    public ResponseEntity updateUserByAdmin(UserPayload userPayload, HttpServletRequest request) {
        User user = findByUsername(userPayload.getUsername());
        User admin = whoAmI(request);

        if (!admin.getRoles().contains(roleRepository.findAllByName("ADMIN"))) {
            return (new ResponseEntity(new Result(false, "user  isn't admin", null), BAD_REQUEST));
        }
        return updateDetails(userPayload, user);
    }


    private boolean checkPasswordLength(String password) {
        return password.length() >= 5;
    }

    private boolean checkUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean checkPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    private boolean checkEmail(String email) {
        return userRepository.existsByEmail(email);
    }

}
