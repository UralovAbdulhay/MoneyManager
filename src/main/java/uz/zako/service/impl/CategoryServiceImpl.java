package uz.zako.service.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.zako.entity.Category;
import uz.zako.entity.User;
import uz.zako.exceptions.ResourceNotFoundException;
import uz.zako.model.Result;
import uz.zako.payload.CategoryPayload;
import uz.zako.repository.CategoryRepository;
import uz.zako.service.CategoryService;

import java.util.List;
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {


    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @Override
    public Category findByName(String name) {
        return categoryRepository.findByName(name).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public <T> List findAll() {
        return categoryRepository.findAll();
    }

    @Override
    public <T> T findById(UUID id) {
        return (T) categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    @Override
    public <T> T save(T t) {
        return (T) categoryRepository.save((Category) t);
    }


    @Override
    public <T> T update(T t) {
        return this.save(t);
    }

    @Override
    public <T> boolean delete(UUID id) {
        categoryRepository.deleteById(id);
        return categoryRepository.existsById(id);
    }

    @Override
    public <T, R> T getObjectFromPayload(R r) {
        return null;
    }

    @Override
    public <T, R> T getPayloadFromObject(R r) {
        return null;
    }

    public ResponseEntity findAllByUserId(User user) {
        return ResponseEntity.ok(categoryRepository.findAllByUsers(user));
    }

    public ResponseEntity findByIdAndUser(UUID id, User user) {
        return ResponseEntity.ok(categoryRepository.findByIdAndUsers(id, user).orElseThrow(() -> new ResourceNotFoundException("Category not found")));
    }


    public ResponseEntity createCategory(CategoryPayload categoryPayload, User user) {

        Category category = new Category();

        if (checkCategoryName(categoryPayload.getName())) {
            return ResponseEntity.badRequest().body(new Result(false, "Category not saved", null));
        }
        category.setName(categoryPayload.getName());
        category.setUsers(user);
        category = categoryRepository.save(category);

        return category != null
                ? ResponseEntity.ok(category)
                : ResponseEntity.badRequest().body(new Result(false, "Category not saved", null));
    }


    public ResponseEntity editCategory(Category category, CategoryPayload categoryPayload, User user) {

        if (!category.getName().equals(categoryPayload.getName())) {
            if (checkCategoryName(categoryPayload.getName())) {
                return ResponseEntity.badRequest().body(new Result(false, "Category not saved", null));
            }
        }

        category.setName(categoryPayload.getName());
        category.setUsers(user);
        category = categoryRepository.save(category);

        return category != null
                ? ResponseEntity.ok(category)
                : ResponseEntity.badRequest().body(new Result(false, "Category not saved", null));
    }


    public ResponseEntity deleteByIdAndUser(UUID id, User user) {
        return categoryRepository.deleteByIdAndUsers(id, user)
                ? ResponseEntity.ok(new Result(true, "category by id = " + id + " was deleted!", null))
                : ResponseEntity.badRequest().body(new Result(false, "category by id = " + id + " wasn't deleted!", null));
    }


    private boolean checkCategoryName(String categoryName) {
        return categoryRepository.existsByName(categoryName);
    }


    public boolean checkCategoryByIdAndUser(UUID id, User user) {
        return categoryRepository.findByIdAndUsers(id, user).isPresent();
    }
}
