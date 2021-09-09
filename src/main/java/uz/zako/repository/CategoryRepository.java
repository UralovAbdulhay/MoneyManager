package uz.zako.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.zako.entity.Category;
import uz.zako.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {

    public Optional<Category> findByName(String name);

    public List<Category> findAllByUsers(User user);

    public Optional<Category> findByIdAndUsers(UUID id, User user);

    boolean existsByName(String name);

    boolean deleteByIdAndUsers(UUID id, User user);

    boolean existsByIdAndActive(UUID id, Boolean active);




}
