package uz.zako.service;

import uz.zako.entity.User;
import uz.zako.entity.superEntity.SuperEntity;

import java.util.List;
import java.util.UUID;

public interface UserService extends SuperService {

    public User findByUsername(String username);

}
