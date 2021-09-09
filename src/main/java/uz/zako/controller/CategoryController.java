package uz.zako.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.zako.entity.User;
import uz.zako.payload.CategoryPayload;
import uz.zako.service.impl.CategoryServiceImpl;
import uz.zako.service.impl.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    /*
     *
     * getAll  +
     * getOne  +
     * create  +
     * edit    +
     * delete  +
     *
     *
     *
     *
     *
     * */

    private final CategoryServiceImpl categoryService;
    private final UserServiceImpl userService;


    @GetMapping("/all")
    public ResponseEntity getAllCategories(HttpServletRequest request) {
        return categoryService.findAllByUserId(userService.whoAmI(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity getCategory(@PathVariable UUID id, HttpServletRequest request) {
        User user = userService.whoAmI(request);
        return categoryService.findByIdAndUser(id, user);
    }

    @PostMapping("/add")
    public ResponseEntity createCategory(@RequestBody CategoryPayload categoryPayload, HttpServletRequest request) {
        return ResponseEntity.ok(categoryService.createCategory(categoryPayload, userService.whoAmI(request)));
    }


    @PutMapping("/{id}")
    public ResponseEntity editCategory(@RequestBody CategoryPayload categoryPayload, HttpServletRequest request) {
        return ResponseEntity.ok(
                categoryService.editCategory(
                        categoryService.findByName(categoryPayload.getName()), categoryPayload, userService.whoAmI(request)
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCategory(@PathVariable UUID id, HttpServletRequest request) {
        return categoryService.deleteByIdAndUser(id, userService.whoAmI(request));
    }

}
















































