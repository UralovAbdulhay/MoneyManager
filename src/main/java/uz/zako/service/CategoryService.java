package uz.zako.service;

import uz.zako.entity.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService extends SuperService {

    public Category findByName(String name);




}
